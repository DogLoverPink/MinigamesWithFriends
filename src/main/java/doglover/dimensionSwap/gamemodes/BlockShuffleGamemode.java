package doglover.dimensionSwap.gamemodes;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.utils.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class BlockShuffleGamemode extends TimeEventBasedGamemode {
    @Override
    public void onGameEnd() {

    }

    static File bannedBlocksFile = new File(DimensionSwap.getGamePlugin().getDataFolder(), "banned-blockshuffle-blocks.yml");
    static FileConfiguration bannedBlocks = YamlConfiguration.loadConfiguration(bannedBlocksFile);
    static List<String> bannedBlocksStringList = bannedBlocks.getStringList("banned-blocks");
    /** Used purely for tab completion */
    static List<Material> unbannedBlocksList = new ArrayList<>();
    static List<String> unbannedBlocksStringList = new ArrayList<>();

    Map<UUID, Material> playerBlocks = new HashMap<>();
    List<UUID> playersWhoHaveSteppedOnBlock = new ArrayList<>();

    public static List<Material> getUnbannedBlocksList() {
        return unbannedBlocksList;
    }


    public static List<String> getUnbannedBlocksStringList() {
        return unbannedBlocksStringList;
    }

    public static List<String> getBannedBlocksStringList() {
        return bannedBlocksStringList;
    }

    static {
        syncBannedBlocksList();
    }

    public static void banBlock(Material block) {
        String name = block.name();
        if (!bannedBlocksStringList.contains(name)) {
            bannedBlocksStringList.add(name);
            bannedBlocks.set("banned-blocks", bannedBlocksStringList);
            saveBannedBlocksFile();
        }
    }

    public static void unbanBlock(Material block) {
        String name = block.name();
        if (bannedBlocksStringList.contains(name)) {
            bannedBlocksStringList.remove(name);
            bannedBlocks.set("banned-blocks", bannedBlocksStringList);
            saveBannedBlocksFile();
        }
    }

    private static void syncBannedBlocksList() {
        List<Material> bannedBlocks = new ArrayList<>();
        for (String blockName : bannedBlocksStringList) {
            Material material = Material.getMaterial(blockName);
            if (material != null) {
                bannedBlocks.add(material);
            }
        }
        List<Material> mats = new ArrayList<>(List.of(Material.values()));
        mats = mats.stream().filter(material -> {
            if (material.isBlock()) {
                return !bannedBlocks.contains(material);
            }
            return false;
        }).toList();
        unbannedBlocksList.clear();
        unbannedBlocksList.addAll(mats);
        unbannedBlocksStringList.clear();
        for (Material material : unbannedBlocksList) {
            unbannedBlocksStringList.add(material.name());
        }
    }



    static void saveBannedBlocksFile() {
        try {
            bannedBlocks.set("banned-blocks", bannedBlocksStringList);
            syncBannedBlocksList();
            bannedBlocks.save(bannedBlocksFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void removeNetherBlocksIfApplicable() {
        if (!getGame().getConfig().getBlockShuffleConfig().shouldAllowNetherBlocks()) {
            unbannedBlocksList.removeIf(BlockUtils::isLikelyNetherBlock);
        }
    }

    public void skip() {
        getGame().broadcast("§aSkipping...");
        onTimeEventTrigger();
        this.setTickGoal(getNextComputedTime());
    }

    @Override
    public void onGameStart() {
        this.setMinTicks(getGame().getConfig().getBlockShuffleConfig().getMinimumSecondsBeforeShuffle() * 20);
        this.setMaxTicks(getGame().getConfig().getBlockShuffleConfig().getMaximumSecondsBeforeShuffle() * 20);
        saveBannedBlocksFile();
        removeNetherBlocksIfApplicable();
        super.onGameStart();
        assignNewBlocks();
    }


    private Material getUnbannedBlock() {
        List<Material> allBlocks = new ArrayList<>(getUnbannedBlocksList());
        java.util.Collections.shuffle(allBlocks);
        return allBlocks.getFirst();
    }

    public void notifyBlockSteppedOn(Player player) {
        Material block = playerBlocks.get(player.getUniqueId());
        if (block != null) {
            if (getGame().getConfig().getBlockShuffleConfig().shouldGivePointsAtEndOfRound()) {
                playersWhoHaveSteppedOnBlock.add(player.getUniqueId());
                playerBlocks.remove(player.getUniqueId());
                getGame().broadcast("§b" + player.getName() + "§a has found their block!");
            } else {
                getGame().addPointsToPlayer(player, getGame().getConfig().getBlockShuffleConfig().getPointsPerSuccessfulBlockStep());
                getGame().broadcast("§b" + player.getName() + "§a has found their block!");
                playerBlocks.remove(player.getUniqueId());
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
                playerBlocks.put(player.getUniqueId(), newBlock);
            }
        } else {
            Material newBlock = getUnbannedBlock();
            for (Player player : getGame().getPlayers()) {
                playerBlocks.put(player.getUniqueId(), newBlock);
            }
        }
        getGame().broadcast("§aBlocks have been shuffled");
        getGame().broadcast("§e----------------------------");
        for (Player player : getGame().getPlayers()) {
            getGame().broadcast("§e" + player.getName() + ": §b" + playerBlocks.get(player.getUniqueId()).name());
        }
        getGame().broadcast("§e----------------------------");
    }

    @Override
    public void tick() {
        super.tick();
        for (Player player : getGame().getPlayers()) {
            if (!playerBlocks.containsKey(player.getUniqueId())) {
                continue;
            }
            Material block = playerBlocks.get(player.getUniqueId());
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
        for (UUID uuid : playersWhoHaveSteppedOnBlock) {
            Player plr = Bukkit.getPlayer(uuid);
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
