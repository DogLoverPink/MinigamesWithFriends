package doglover.dimensionSwap.utils;

import org.bukkit.entity.Player;

public class PlayerUtils {

    /**
     * Method to reset player as if they just joined the server.
     * This method will reset the player's inventory, health, hunger, and other attributes.
     */
    public static void resetPlayer(Player plr) {
        plr.getInventory().clear();
        plr.setHealth(20);
        plr.setFoodLevel(20);
        plr.clearActivePotionEffects();

    }
}
