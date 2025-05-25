package doglover.dimensionSwap.gamemodes;

import doglover.dimensionSwap.DimensionSwap;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockShuffleGamemode extends TimeEventBasedGamemode {
    @Override
    public void onGameEnd() {
        super.onGameEnd();
    }

    static File bannedBlocksFile = new File(DimensionSwap.getGamePlugin().getDataFolder(), "banned-blockshuffle-blocks.yml");
    static FileConfiguration bannedBlocks = YamlConfiguration.loadConfiguration(bannedBlocksFile);
    static List<Material> bannedBlocksList = (List<Material>) bannedBlocks.getList("banned-blocks", new ArrayList<Material>());

    Map<Player, Material> playerBlocks = new HashMap<>();
    List<Player> playersWhoHaveSteppedOnBlock = new ArrayList<>();


    static void banBlock(Material block) {
        if (!bannedBlocksList.contains(block)) {
            bannedBlocksList.add(block);
            bannedBlocks.set("banned-blocks", bannedBlocksList);
            saveBannedBlocksFile();
        }
    }

    static void unbanBlock(Material block) {
        if (bannedBlocksList.contains(block)) {
            bannedBlocksList.remove(block);
            bannedBlocks.set("banned-blocks", bannedBlocksList);
            saveBannedBlocksFile();
        }
    }

    static void saveBannedBlocksFile() {
        try {
            bannedBlocks.set("banned-blocks", bannedBlocksList);
            bannedBlocks.save(bannedBlocksFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGameStart() {
        this.setMinTicks(getGame().getConfig().getBlockShuffleConfig().getMinimumSecondsBeforeShuffle() * 20);
        this.setMaxTicks(getGame().getConfig().getBlockShuffleConfig().getMaximumSecondsBeforeShuffle() * 20);
        saveBannedBlocksFile();
        super.onGameStart();
        bannedBlocksList = (List<Material>) bannedBlocks.getList("banned-blocks", new ArrayList<Material>());
        assignNewBlocks();
    }


    private Material getUnbannedBlock() {
        List<Material> allBlocks = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isBlock() && !bannedBlocksList.contains(material)) {
                allBlocks.add(material);
            }
        }
        java.util.Collections.shuffle(allBlocks);
        return allBlocks.getFirst();
    }

    public void notifyBlockSteppedOn(Player player) {
        Material block = playerBlocks.get(player);
        if (block != null) {
            if (getGame().getConfig().getBlockShuffleConfig().shouldGivePointsAtEndOfRound()) {
                playersWhoHaveSteppedOnBlock.add(player);
                playerBlocks.remove(player);
                getGame().broadcast("§b" + player.getName() + "§a has found their block!");
            } else {
                getGame().addPointsToPlayer(player, getGame().getConfig().getBlockShuffleConfig().getPointsPerSuccessfulBlockStep());
                getGame().broadcast("§b" + player.getName() + "§a has found their block!");
                playerBlocks.remove(player);
            }
            if (playerBlocks.isEmpty()) {
                onTimeEventTrigger();
                this.setTickGoal(getNextComputedTime());
            }
        }
    }

    private void assignNewBlocks() {
        playerBlocks.clear();
        if (getGame().getConfig().getBlockShuffleConfig().shouldShuffleBlocksPerPlayer()) {
            for (Player player : getGame().getPlayers()) {
                Material newBlock = getUnbannedBlock();
                playerBlocks.put(player, newBlock);
            }
        } else {
            Material newBlock = getUnbannedBlock();
            for (Player player : getGame().getPlayers()) {
                playerBlocks.put(player, newBlock);
            }
        }
        getGame().broadcast("§aBlocks have been shuffled");
        getGame().broadcast("§e----------------------------");
        for (Player player : getGame().getPlayers()) {
            getGame().broadcast("§e" + player.getName() + ": §b" + playerBlocks.get(player).name());
        }
        getGame().broadcast("§e----------------------------");
    }

    @Override
    public void tick() {
        super.tick();
        for (Player player : getGame().getPlayers()) {
            if (!playerBlocks.containsKey(player)) {
                continue;
            }
            Material block = playerBlocks.get(player);
            if (player.getLocation().clone().subtract(0, 1, 0).getBlock().getType() == block) {
                notifyBlockSteppedOn(player);
            }
        }
        getGame().addScoreboardContributution("§dBlock Shuffle in: §b" + getFormattedTimeRemaining());
        if (!getGame().getConfig().getBlockShuffleConfig().shouldShuffleBlocksPerPlayer()) {
            if (!playerBlocks.isEmpty()) {
                getGame().addScoreboardContributution("§dTarget Block: §b" + playerBlocks.values().toArray()[0]);
            }
        }
    }

    @Override
    public void onTimeEventTrigger() {
        for (Player plr : playersWhoHaveSteppedOnBlock) {
            getGame().addPointsToPlayer(plr, getGame().getConfig().getBlockShuffleConfig().getPointsPerSuccessfulBlockStep());
        }
        playersWhoHaveSteppedOnBlock.clear();
        assignNewBlocks();
    }

    @Override
    public String toString() {
        return "BlockShuffle";
    }
}
