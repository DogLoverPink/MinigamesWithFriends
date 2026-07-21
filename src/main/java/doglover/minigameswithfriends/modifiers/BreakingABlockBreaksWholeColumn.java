package doglover.minigameswithfriends.modifiers;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.configs.modifierconfigs.BreakingABlockBreaksWholeColumnConfig;
import doglover.minigameswithfriends.gamemodes.Modifier;
import doglover.minigameswithfriends.utils.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class BreakingABlockBreaksWholeColumn extends Modifier {

    private static final BreakingABlockBreaksWholeColumnConfig CONFIG = new BreakingABlockBreaksWholeColumnConfig();

    static {
        Modifier.register("BreakingABlockBreaksWholeColumn", BreakingABlockBreaksWholeColumn.class, BreakingABlockBreaksWholeColumn::new, CONFIG);
    }

    @Override
    public BreakingABlockBreaksWholeColumnConfig getConfig() {
        return CONFIG;
    }


    public BreakingABlockBreaksWholeColumn() {
        subscribeToEvent(BlockBreakEvent.class);
    }

    private final List<BukkitTask> runningTasks = new ArrayList<>();

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        int yLevel = event.getBlock().getY();
        List<Block> blocksToBreak = new ArrayList<>();
        if (getConfig().getBreakDirection().equalsIgnoreCase("AboveAndBelow")) {
            yLevel = 320;
        }
        boolean shouldDropItems = getConfig().shouldDropItems();
        for (int i = yLevel; i >= -64; i--) {
            if (i == event.getBlock().getY()) {
                continue;
            }
            blocksToBreak.add(event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), i, event.getBlock().getZ()));
        }
        blocksToBreak.stream().filter(Block::isLiquid).forEach(block -> block.setType(Material.AIR, false));
        if (getConfig().getAnimationMode().equals("None")) {
            blocksToBreak.forEach(block -> BlockUtils.breakBlockWithParticlesAndSounds(block, true, true, shouldDropItems));
        } else if (getConfig().getAnimationMode().equals("FallingBlocks")) {
            blocksToBreak.forEach(block -> BlockUtils.convertBlockToFallingBlock(block, false));
        } else if (getConfig().getAnimationMode().equals("RapidBreaking")) {
            Deque<Block> queue = blocksToBreak.stream().filter(block -> !block.getType().isAir()).collect(Collectors.toCollection(ArrayDeque::new));
            runningTasks.add(new BukkitRunnable() {
                        @Override
                        public void run() {
                            Block nextBlock = queue.poll();
                            if (nextBlock == null) {
                                this.cancel();
                                return;
                            }
                            BlockUtils.breakBlockWithParticlesAndSounds(nextBlock, true, true, shouldDropItems);
                        }

            }.runTaskTimer(MinigamesWithFriends.getGamePlugin(), 0L, 0L));
        }
    }

    @Override
    public void tick() {
        runningTasks.removeIf(BukkitTask::isCancelled);

    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
        for  (BukkitTask task : runningTasks) {
            if (task.isCancelled()) return;
            task.cancel();
        }

    }

    @Override
    public void updateConfig() {

    }

    @Override
    public String toString() {
        return "BreakingABlockBreaksWholeColumn";
    }
}
