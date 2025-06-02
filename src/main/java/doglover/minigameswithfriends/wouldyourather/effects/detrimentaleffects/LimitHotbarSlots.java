package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LimitHotbarSlots extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(LimitHotbarSlots.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You can only use 4 hotbar slots";
    }

    public LimitHotbarSlots(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerAttemptPickupItemEvent.class);
    }

    @Override
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (!playerHasRoom(event.getItem().getItemStack())) {
            event.setCancelled(true);
        }
    }

    private boolean playerHasRoom(ItemStack item) {
        for (int i = 0; i < 36; i++) {
            if (i >= 4 && i < 9) {
                continue;
            }
            ItemStack otherItem = getPlayer().getInventory().getItem(i);
            if (otherItem == null || (otherItem.isSimilar(item) && otherItem.getAmount() + item.getAmount() <= otherItem.getMaxStackSize())) {
                return true;
            }
        }
        return false;
    }


    private void clearOutSlots() {
        outerLoop:
        for (int i = 4; i < 9; i++) {
            ItemStack item = getPlayer().getInventory().getItem(i);
            if (item == null) {
                continue;
            }
            for (int j = 9; j < 36; j++) {
                ItemStack otherItem = getPlayer().getInventory().getItem(j);
                if (otherItem == null) {
                    getPlayer().getInventory().setItem(j, item);
                    getPlayer().getInventory().setItem(i, null);
                    continue outerLoop;
                }
                if (item.isSimilar(otherItem) && item.getAmount() + otherItem.getAmount() <= otherItem.getMaxStackSize()) {
                    getPlayer().getInventory().getItem(j).setAmount(item.getAmount() + otherItem.getAmount());
                    getPlayer().getInventory().setItem(i, null);
                    continue outerLoop;
                }
            }
            player.getWorld().dropItemNaturally(player.getLocation(), item);
            getPlayer().getInventory().setItem(i, null);
        }
    }

    @Override
    public void on4HertzTick() {
        clearOutSlots();
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
