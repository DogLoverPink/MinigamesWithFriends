package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class StealItemFromPlayer extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(StealItemFromPlayer.class);
    }

    boolean isPicking = false;
    Player randPlayer = null;
    Inventory inventory = null;

    @Override
    public String getDescriptionBlurb() {
        return "Get to take your friend's most prized procession.";
    }

    public StealItemFromPlayer(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(InventoryClickEvent.class);
        subscribeToEvent(InventoryCloseEvent.class);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().equals(getPlayer())) {
            return;
        }

        if (isPicking) {
            if (!(event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.PLACE_ALL)) {
                event.setCancelled(true);
                return;
            }
            if (event.getInventory().equals(event.getView().getTopInventory())) {
                ItemStack item = event.getCurrentItem();
                if (item != null) {
                    ItemUtils.giveItemsToPlayer(getPlayer(), item);
                    event.setCancelled(true);
                    isPicking = false;
                    event.getInventory().remove(item);
                    event.getInventory().close();
                    if (randPlayer != null) {
                        getPlayer().sendMessage(Component.text("§aYou took §b" + item.getAmount() + " §e" + item.getType() + "§a from §9" + randPlayer.getName() + "!"));
                        randPlayer.sendMessage(Component.text("§9" + getPlayer().getName() + "§c took §b" + item.getAmount() + " §e" + item.getType() + " §cfrom you!"));
                    }

                    this.selfDestruct();
                }

            }
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (isPicking) {
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
                getPlayer().openInventory(inventory);
            }, 1);
        }
    }


    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        ArrayList<Player> plrs = new ArrayList<>(MinigamesWithFriends.getGame().getPlayers());
        plrs.remove(getPlayer());
        plrs.removeIf(p -> p.getInventory().isEmpty());
        if (!plrs.isEmpty()) {
            randPlayer = plrs.get(random.nextInt(plrs.size()));
            inventory = randPlayer.getInventory();
            getPlayer().openInventory(inventory);
        } else {
            inventory = getPlayer().getInventory();
            if (inventory.isEmpty()) {
                this.selfDestruct();
                return;
            }
            getPlayer().openInventory(getPlayer().getInventory());
        }
        isPicking = true;
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
