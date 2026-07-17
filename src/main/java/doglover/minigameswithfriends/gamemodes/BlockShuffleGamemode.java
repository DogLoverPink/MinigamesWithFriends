package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.commands.CommandHandler;
import doglover.minigameswithfriends.configs.BlockShuffleConfig;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

import static doglover.minigameswithfriends.commands.BuiltInCommandDefinitions.filterByStartsWith;

public class BlockShuffleGamemode extends TimeEventBasedGamemode {


    private static final BlockShuffleConfig CONFIG = new BlockShuffleConfig();

    static {
        Gamemode.register("BlockShuffle", BlockShuffleGamemode.class, BlockShuffleGamemode::new, CONFIG);
        CommandHandler.registerCommand(
                "BlockShuffle",
                BlockShuffleGamemode::handleBlockShuffleCommand,
                BlockShuffleGamemode::handleBlockShuffleCompletions);
    }

    @Override
    public BlockShuffleConfig getConfig() {
        return CONFIG;
    }

    private static void handleBlockShuffleCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("BanBlock")) {
            if (args.length == 3) {
                String blockName = args[2];
                Material block = Material.getMaterial(blockName);
                if (block == null) {
                    commandSender.sendMessage("§cInvalid block name. Must be uppercase enum style");
                    return;
                }
                BlockShuffleGamemode.banBlock(block);
                commandSender.sendMessage("§aBlock " + blockName + " banned.");
            } else {
                commandSender.sendMessage("§cPlease specify a block name.");
            }
        } else if (args[1].equalsIgnoreCase("UnbanBlock")) {
            if (args.length == 3) {
                String blockName = args[2];
                Material block = Material.getMaterial(blockName);
                if (block == null) {
                    commandSender.sendMessage("§cInvalid block name. Must be uppercase enum style");
                    return;
                }
                BlockShuffleGamemode.unbanBlock(block);
                commandSender.sendMessage("§aBlock " + blockName + " unbanned.");
            } else {
                commandSender.sendMessage("§cPlease specify a block name.");
            }
        } else if (args[1].equalsIgnoreCase("ListBannedBlocks")) {
            commandSender.sendMessage("§eBanned blocks: §b");
            for (String materialName : BlockShuffleGamemode.getBannedBlocksStringList()) {
                commandSender.sendMessage("§b" + materialName);
            }
        } else if (args[1].equalsIgnoreCase("skip")) {
            if (!MinigamesWithFriends.getGame().isRunning() || !MinigamesWithFriends.getGame().isGamemodeActive(BlockShuffleGamemode.class)) {
                return;
            }
            MinigamesWithFriends.getGame().getGamemode(BlockShuffleGamemode.class).skip();
        }
    }

    private static List<String> handleBlockShuffleCompletions(String[] args) {
        if (args.length == 2) {
            return filterByStartsWith(List.of("BanBlock", "UnbanBlock", "ListBannedBlocks", "Skip"), args[1]);
        }
        String blockshuffleCommand = args[1];
        if (blockshuffleCommand.equalsIgnoreCase("UnbanBlock")) {
            return filterByStartsWith(BlockShuffleGamemode.getBannedBlocksStringList(), args[2]);
        } else if (blockshuffleCommand.equalsIgnoreCase("BanBlock")) {
            return filterByStartsWith(BlockShuffleGamemode.getUnbannedBlocksStringList(), args[2]);
        } else if (blockshuffleCommand.equalsIgnoreCase("ListBannedBlocks")) {
            return BlockShuffleGamemode.getBannedBlocksStringList();
        } else {
            return List.of();
        }
    }

    @Override
    public void onGameEnd() {

    }

    static File bannedBlocksFile = new File(MinigamesWithFriends.getGamePlugin().getDataFolder(), "banned-blockshuffle-blocks.yml");
    static FileConfiguration bannedBlocks = YamlConfiguration.loadConfiguration(bannedBlocksFile);
    static List<String> bannedBlocksStringList = bannedBlocks.getStringList("banned-blocks");
    /**
     * Used purely for tab completion
     */
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
        if (!getConfig().shouldAllowNetherBlocks()) {
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
        updateConfig();
        saveBannedBlocksFile();
        removeNetherBlocksIfApplicable();
        super.onGameStart();
        assignNewBlocks();
    }

    @Override
    public void updateConfig() {
        this.setMinTicks(getConfig().getMinimumSecondsBeforeShuffle() * 20);
        this.setMaxTicks(getConfig().getMaximumSecondsBeforeShuffle() * 20);
    }


    private Material getUnbannedBlock() {
        List<Material> allBlocks = new ArrayList<>(getUnbannedBlocksList());
        java.util.Collections.shuffle(allBlocks);
        return allBlocks.getFirst();
    }

    public void notifyBlockSteppedOn(Player player) {
        Material block = playerBlocks.get(player.getUniqueId());
        if (block == null) {
            return;
        }
        ParticleUtils.createParticleCloud(player.getLocation(), 3, Particle.FIREWORK, 75);
        if (getConfig().shouldGivePointsAtEndOfRound()) {
            playersWhoHaveSteppedOnBlock.add(player.getUniqueId());
            playerBlocks.remove(player.getUniqueId());
            getGame().broadcast("§b" + player.getName() + "§a has found their block!");
        } else {
            getGame().addPointsToPlayer(player, getConfig().getPointsPerSuccessfulBlockStep());
            getGame().broadcast("§b" + player.getName() + "§a has found their block!");
            playerBlocks.remove(player.getUniqueId());
        }
        if (playerBlocks.isEmpty()) {
            onTimeEventTrigger();
            this.setTickGoal(getNextComputedTime());
        }
        for (Player plr : getGame().getPlayers()) {
            if (plr.equals(player)) {
                plr.playSound(plr.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            } else {
                plr.playSound(plr.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1, 1);
            }
        }
    }

    private void assignNewBlocks() {
        playerBlocks.clear();
        if (getConfig().shouldShuffleBlocksPerPlayer()) {
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
    public void onPlayerJoin(Player plr) {
        if (getConfig().shouldShuffleBlocksPerPlayer()) {
            Material newBlock = getUnbannedBlock();
            playerBlocks.put(plr.getUniqueId(), newBlock);
        } else {
            if (playerBlocks.isEmpty()) {
                assignNewBlocks();
                return;
            }
            Material currentMaterial = (Material) playerBlocks.values().toArray()[0];
            playerBlocks.put(plr.getUniqueId(), currentMaterial);
        }
    }

    @Override
    public void onPlayerLeave(Player plr) {
        playerBlocks.remove(plr.getUniqueId());
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
        getGame().addScoreboardContribution("§dBlock Shuffle in: §b" + getFormattedTimeRemaining());
        if (!getConfig().shouldShuffleBlocksPerPlayer()) {
            if (!playerBlocks.isEmpty()) {
                getGame().addScoreboardContribution("§dTarget Block: §b" + playerBlocks.values().toArray()[0]);
            }
        } else {
            int i = 0;
            for (Map.Entry<UUID, Material> entry : playerBlocks.entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (i > 4) {
                    getGame().addScoreboardContribution("§dAnd " + (playerBlocks.size() - 5) + " more...");
                    break;
                }
                if (player != null) {
                    getGame().addScoreboardContribution("§d" + player.getName() + "'s Block: §b" + entry.getValue().name());
                }
                i++;
            }
        }
    }

    @Override
    public void onTimeEventTrigger() {
        for (UUID uuid : playersWhoHaveSteppedOnBlock) {
            Player plr = Bukkit.getPlayer(uuid);
            getGame().addPointsToPlayer(plr, getConfig().getPointsPerSuccessfulBlockStep());
        }
        playersWhoHaveSteppedOnBlock.clear();
        assignNewBlocks();
    }

    @Override
    public String toString() {
        return "BlockShuffle";
    }
}
