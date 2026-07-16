package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ArrayIndexesStartAtOne extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(ArrayIndexesStartAtOne.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Array indexes start at one";
    }

    public ArrayIndexesStartAtOne(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(InventoryClickEvent.class, EventPriority.LOW);
    }


    private int getAdjustedSlot(int slot, int max) {
        if (slot == max) {
            return 0;
        }
        return slot + 1;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!player.equals(getPlayer()) || event.getSlot() > 35 || event.getCurrentItem() == null) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (event.getClick() != ClickType.LEFT) {
            return;
        }

        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }

        if ((event.getSlot() > 35 && inventory.getType() == InventoryType.PLAYER) || inventory.getType() == InventoryType.CRAFTING) {
            return;
        }
        event.setCancelled(true);
        int invSize = inventory.getSize() - 1;
        if (inventory.getType() == InventoryType.PLAYER) {
            invSize = 35;
        }
        int slotIndex = getAdjustedSlot(event.getSlot(), invSize);
        boolean isCursorClear = getPlayer().getItemOnCursor().isEmpty();
        ItemStack newItem = inventory.getItem(slotIndex);

        if (isCursorClear) {
            getPlayer().setItemOnCursor(newItem);
            inventory.clear(slotIndex);
        } else {
            ItemStack oldItem = getPlayer().getItemOnCursor().clone();
            getPlayer().setItemOnCursor(newItem);
            inventory.setItem(slotIndex, oldItem);
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
