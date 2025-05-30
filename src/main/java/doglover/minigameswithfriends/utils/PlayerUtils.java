package doglover.minigameswithfriends.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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


    public static void launchPlayerToLoc(Player plr, Location to) {
        Location from = plr.getLocation();
        Vector vector = to.toVector().subtract(from.toVector()).normalize();
        vector = vector.add(new Vector(0, 0.05, 0));
        vector = vector.multiply(new Vector(3, 6, 3));
        plr.setVelocity(vector);
    }
}
