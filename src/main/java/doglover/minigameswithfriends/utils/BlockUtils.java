package doglover.minigameswithfriends.utils;

import doglover.minigameswithfriends.MinigamesWithFriends;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;
import java.util.stream.Collectors;

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

    public static Location findLocationOfBlockType(List<Material> types, Location startLoc, int radius) {
        World world = startLoc.getWorld();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = startLoc.clone().add(x, y, z);
                    Block block = loc.getBlock();
                    if (types.contains(block.getType())) {
                        return loc;
                    }
                }
            }
        }
        return null;
    }

    public static List<Location> getNearbyNonAirBlocksOfType(Location loc, Material material, double radius) {
        World world = loc.getWorld();
        return getNearbyNonAirBlocks(loc, radius).stream().filter(
                (blockLoc) -> world.getBlockAt(blockLoc).getType().equals(material)
        ).collect(Collectors.toList());
    }

    public static List<Location> getNearbyNonAirBlocks(Location loc, double radius) {
        List<Location> found = new java.util.ArrayList<>();
        for (double x = -radius; x <= radius; x++) {
            for (double y = -radius; y <= radius; y++) {
                for (double z = -radius; z <= radius; z++) {
                    Location checkLoc = loc.clone().add(x, y, z);
                    Block block = checkLoc.getBlock();
                    if (!block.getType().isAir()) {
                        found.add(checkLoc);
                    }
                }
            }
        }
        return found;
    }

    public static void createWallsAroundLocation(Location loc, int size) {
        //Get the 4 corner locations of the wall, size blocks away from the center
        Location loc1 = loc.clone().add(size, size, size);
        Location loc2 = loc.clone().add(size, -size, -size);
        Location loc3 = loc.clone().add(-size, size, -size);
        Location loc4 = loc.clone().add(-size, -size, size);
        //Set the blocks between the 4 corner locations to wool
        replaceBlocksBetween(loc1, loc2, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc2, loc3, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc3, loc4, Material.RED_WOOL, Material.AIR);
        replaceBlocksBetween(loc4, loc1, Material.RED_WOOL, Material.AIR);
    }

    /**
     * Finds any blocks specified in the find list, and replaces them with the replaceWith material, then will replace them back after 20 ticks.
     * Set find to null to change any non-air block
     */
    public static void removeBlockOfTypeNearThenReplace(Location loc, Material replaceWith, Material... find) {
        List<Material> findList = null;
        if (find != null) {
            findList = new ArrayList<>(Arrays.asList(find));
            if (findList.contains(Material.AIR)) {
                findList.add(Material.CAVE_AIR);
                findList.add(Material.VOID_AIR);
            }
        }
        Map<Location, Material> removedBlocks = new HashMap<>();
        int radius = 2;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location newLoc = loc.clone().add(x, y, z);
                    Block newBlock = newLoc.getBlock();
                    if (
                            (findList == null && !newBlock.getType().isAir())
                                    || (findList != null && findList.contains(newBlock.getType()))
                    ) {
                        removedBlocks.put(newLoc, newBlock.getType());
                        newBlock.setType(replaceWith);
                    }
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
            for (Map.Entry<Location, Material> entry : removedBlocks.entrySet()) {
                entry.getKey().getBlock().setType(entry.getValue());
            }
        }, 20L);
    }

    public static void removeWallsAroundLocation(Location loc, int size) {
        //Get the 4 corner locations of the wall, 35 blocks away from the center
        Location loc1 = loc.clone().add(size, size, size);
        Location loc2 = loc.clone().add(size, -size, -size);
        Location loc3 = loc.clone().add(-size, size, -size);
        Location loc4 = loc.clone().add(-size, -size, size);
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

    /**
     * Finds a safe location for teleporting within the specified radius
     *
     * @param loc    The center location to search around
     * @param radius The radius to search within
     * @return A safe location, or the input location if no safe location was found
     */
    public static Location findSafeLocationInRadius(Location loc, int radius) {
        if (radius < 0 || loc == null || loc.getWorld() == null) {
            return loc;
        }
        for (int r = 0; r <= radius; r++) {
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if (Math.abs(x) != r && Math.abs(z) != r) continue;

                    for (int y = -r; y <= r; y++) {
                        Location checkLoc = loc.clone().add(x, y, z);
                        if (checkLoc.getBlock().getType().isSolid() &&
                                !checkLoc.clone().add(0, 1, 0).getBlock().isSuffocating() &&
                                !checkLoc.clone().add(0, 2, 0).getBlock().isSuffocating()) {
                            return checkLoc.clone().add(0, 1, 0);
                        }
                    }
                }
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
