package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.*;

public class VeinMiner extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(VeinMiner.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to vein mine with any tool";
    }

    public VeinMiner(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(BlockBreakEvent.class);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        ItemStack mainHand = getPlayer().getInventory().getItemInMainHand();
        if (!Tag.ITEMS_ENCHANTABLE_MINING_LOOT.isTagged(mainHand.getType())) {
            return;
        }
        int durability = mainHand.getType().getMaxDurability() - ((Damageable) mainHand.getItemMeta()).getDamage();
        for (Block block : getNearbyBlocksToVeinMine(event.getBlock().getLocation(), durability)) {
            block.breakNaturally();
            mainHand.damage(1, getPlayer());
        }
    }

    private List<Block> getNearbyBlocksToVeinMine(Location location, int maxCount) {
        Set<Block> foundBlocks = new LinkedHashSet<>();
        Block startBlock = location.getBlock();
        Material targetType = startBlock.getType();

        findConnectedBlocks(startBlock, targetType, foundBlocks, maxCount);

        return new ArrayList<>(foundBlocks);
    }

    private void findConnectedBlocks(Block startBlock, Material type, Set<Block> foundBlocks, int maxCount) {
        if (maxCount <= 0) return;

        Queue<Block> queue = new ArrayDeque<>();
        Set<Block> visited = new HashSet<>();

        queue.add(startBlock);
        visited.add(startBlock);

        final int[][] DIRS = new int[][]{
                {1, 0, 0}, {-1, 0, 0},
                {0, 1, 0}, {0, -1, 0},
                {0, 0, 1}, {0, 0, -1}
        };

        while (!queue.isEmpty() && foundBlocks.size() < maxCount) {
            Block block = queue.poll();
            if (block == null) continue;

            if (!block.getType().equals(type)) continue;

            foundBlocks.add(block);
            if (foundBlocks.size() >= maxCount) break;

            for (int[] d : DIRS) {
                Block relative = block.getRelative(d[0], d[1], d[2]);
                if (!visited.contains(relative)) {
                    visited.add(relative);
                    queue.add(relative);
                }
            }
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
