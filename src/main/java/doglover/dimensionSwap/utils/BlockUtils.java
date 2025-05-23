package doglover.dimensionSwap.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockUtils {


    public static void createHollowBoxAroundLocation(Location loc) {
        Location loc1 = loc.clone().add(2, 2, 2);
        Location loc2 = loc.clone().add(-2, -2, 2);
        Location loc3 = loc.clone().add(-2, 2, -2);
        Location loc4 = loc.clone().add(2, -2, -2);

        replaceBlocksBetween(loc1, loc2, Material.RED_STAINED_GLASS, Material.AIR);
        replaceBlocksBetween(loc2, loc3, Material.RED_STAINED_GLASS, Material.AIR);
        replaceBlocksBetween(loc3, loc4, Material.RED_STAINED_GLASS, Material.AIR);
        replaceBlocksBetween(loc4, loc1, Material.RED_STAINED_GLASS, Material.AIR);
        replaceBlocksBetween(loc1, loc3, Material.RED_STAINED_GLASS, Material.AIR);
        replaceBlocksBetween(loc2, loc4, Material.RED_STAINED_GLASS, Material.AIR);
    }

    public static void removeHollowBoxAroundLocation(Location loc) {
        Location loc1 = loc.clone().add(2, 2, 2);
        Location loc2 = loc.clone().add(-2, -2, -2);

        replaceBlocksBetween(loc1, loc2, Material.AIR, Material.RED_STAINED_GLASS);

    }

    public static void createWallsAroundLocation(Location loc) {
        //Get the 4 corner locations of the wall, 35 blocks away from the center
        Location loc1 = loc.clone().add(35, 8, 35);
        Location loc2 = loc.clone().add(35, -8, -35);
        Location loc3 = loc.clone().add(-35, 8, -35);
        Location loc4 = loc.clone().add(-35, -8, 35);
        //Set the blocks between the 4 corner locations to wool
        replaceBlocksBetween(loc1, loc2, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc2, loc3, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc3, loc4, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc4, loc1, Material.RED_WOOL, Material.AIR);
    }

    public static void removeWallsAroundLocation(Location loc) {
        //Get the 4 corner locations of the wall, 35 blocks away from the center
        Location loc1 = loc.clone().add(35, 8, 35);
        Location loc2 = loc.clone().add(35, -8, -35);
        Location loc3 = loc.clone().add(-35, 8, -35);
        Location loc4 = loc.clone().add(-35, -8, 35);
        //Set the blocks between the 4 corner locations to wool
        replaceBlocksBetween(loc1, loc2, Material.AIR, Material.RED_WOOL);
        replaceBlocksBetween(loc2, loc3, Material.AIR, Material.RED_WOOL);
        replaceBlocksBetween(loc3, loc4, Material.AIR, Material.RED_WOOL);
        replaceBlocksBetween(loc4, loc1, Material.AIR, Material.RED_WOOL);
    }

    public static void replaceBlocksBetween(Location loc1, Location loc2, Material replaceWith, Material... find) {
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world.");
        }
        List<Material> findList = new ArrayList<>(Arrays.asList(find));
        if (findList.contains(Material.AIR)) {
            findList.add(Material.CAVE_AIR);
            findList.add(Material.VOID_AIR);
        }

        World world = loc1.getWorld();

        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        int x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (findList.contains(block.getType())) {
                        block.setType(replaceWith);
                    }
                }
            }
        }
    }

}
