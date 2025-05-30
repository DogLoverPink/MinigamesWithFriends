package doglover.dimensionSwap.utils;

import doglover.dimensionSwap.DimensionSwap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public class BlockUtils {

    //Credit to matistan05 on github for this, thanks mate! https://github.com/Matistan/MinecraftBlockShuffle/blob/be5edd528efd75d0ef30492f068121c5e5c19214/src/main/java/me/matistan05/minecraftblockshuffle/commands/BlockShuffleCommand.java#L460
    public static boolean isLikelyNetherBlock(Material block) {
        String matName = block.name();
        return matName.contains("NETHER") || matName.contains("CRIMSON") || matName.contains("WARPED") || matName.contains("TWISTING") ||
                matName.contains("WEEPING") || matName.equals("SHROOMLIGHT") || matName.contains("BLACKSTONE") || matName.contains("QUARTZ") ||
                matName.contains("SOUL") || matName.contains("BASALT") || matName.equals("GLOWSTONE") || matName.equals("REDSTONE_LAMP");
    }

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
        Location loc1 = loc.clone().add(35, 35, 35);
        Location loc2 = loc.clone().add(35, -35, -35);
        Location loc3 = loc.clone().add(-35, 35, -35);
        Location loc4 = loc.clone().add(-35, -35, 35);
        //Set the blocks between the 4 corner locations to wool
        replaceBlocksBetween(loc1, loc2, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc2, loc3, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc3, loc4, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc4, loc1, Material.RED_WOOL, Material.AIR);
    }

    public static void removeBlockOfTypeNearThenReplace(Location loc, Material replaceWith, Material... find) {
        List<Material> findList = new ArrayList<>(Arrays.asList(find));
        if (findList.contains(Material.AIR)) {
            findList.add(Material.CAVE_AIR);
            findList.add(Material.VOID_AIR);
        }
        Map<Location, Material>  removedBlocks = new HashMap<>();
        int radius = 2;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location newLoc = loc.clone().add(x, y, z);
                    Block newBlock = newLoc.getBlock();
                    if (findList.contains(newBlock.getType())) {
                        removedBlocks.put(newLoc, newBlock.getType());
                        newBlock.setType(replaceWith);
                    }
                }
            }
        }
        // Restore the removed blocks after a delay
        Bukkit.getScheduler().runTaskLater(DimensionSwap.getGamePlugin(), () -> {
            for (Map.Entry<Location, Material> entry : removedBlocks.entrySet()) {
                entry.getKey().getBlock().setType(entry.getValue());
            }
        }, 20L);
    }

    public static void removeWallsAroundLocation(Location loc) {
        //Get the 4 corner locations of the wall, 35 blocks away from the center
        Location loc1 = loc.clone().add(35, 35, 35);
        Location loc2 = loc.clone().add(35, -35, -35);
        Location loc3 = loc.clone().add(-35, 35, -35);
        Location loc4 = loc.clone().add(-35, -35, 35);
        //Set the blocks between the 4 corner locations to wool
        replaceBlocksBetween(loc1, loc2, Material.AIR, Material.RED_WOOL);
        replaceBlocksBetween(loc2, loc3, Material.AIR, Material.RED_WOOL);
        replaceBlocksBetween(loc3, loc4, Material.AIR, Material.RED_WOOL);
        replaceBlocksBetween(loc4, loc1, Material.AIR, Material.RED_WOOL);
    }

    public static Location findSafeBlock(Location loc) {
        if (loc.clone().subtract(0, 1, 0).getBlock().getType().isSolid()
            && !loc.clone().add(0, 1, 0).getBlock().isSuffocating()
            && !loc.clone().add(0, 2, 0).getBlock().isSuffocating()) {
            return loc;
        }
        for (int i = 0; i <= 200; i++) {
            Location footBlock = loc.clone().add(0, i, 0);
            Location blockAbove = loc.clone().add(0, i + 1, 0);
            Location block2Above = loc.clone().add(0, i + 2, 0);
            if (footBlock.getBlock().getType().isSolid() && !blockAbove.getBlock().isSuffocating() && !block2Above.getBlock().isSuffocating()) {
                return blockAbove;
            }
        }
        return loc;
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
