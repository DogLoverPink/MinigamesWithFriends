package doglover.minigameswithfriends.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public class ItemUtils {

    public static void giveItemsToPlayer(Player player, List<ItemStack> items) {
        Collection<ItemStack> itemsToDrop = player.getInventory().addItem(items.toArray(ItemStack[]::new)).values();
        for (ItemStack item : itemsToDrop) {
            player.getWorld().dropItem(player.getLocation(), item);
        }
    }

    public static void giveItemsToPlayer(Player player, ItemStack item) {
        Collection<ItemStack> itemsToDrop = player.getInventory().addItem(item).values();
        for (ItemStack extraItem : itemsToDrop) {
            player.getWorld().dropItem(player.getLocation(), extraItem);
        }
    }
}
