package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.commands.CommandHandler;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.WorldFileUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static doglover.minigameswithfriends.commands.BuiltInCommandDefinitions.filterByStartsWith;

public class DimensionSwapGamemode extends TimeEventBasedGamemode {

    static {
        CommandHandler.registerCommand(
                "DimensionSwap",
                DimensionSwapGamemode::handleDimensionSwapCommand,
                DimensionSwapGamemode::handleDimensionSwapCompletions);
    }

    private static void handleDimensionSwapCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("PreLoadSavedDimensionSwapWorlds")) {
            DimensionSwapGamemode.preLoadSavedWorlds(commandSender);
        } else {
            commandSender.sendMessage("§cInvalid subcommand!");
        }
    }

    private static List<String> handleDimensionSwapCompletions(String[] args) {
        return filterByStartsWith(List.of("PreLoadSavedDimensionSwapWorlds"), args[1]);
    }


    static File savedWorldsFolder;
    static File activeWorldsFolder;
    List<String> yetToBeUsedWorlds = new ArrayList<>();
    private int maxRounds;
    private int currentRound = 0;


    public static void initialize() {
        savedWorldsFolder = new File( "world/dimensions/minecraft/saveddimensionswapworlds");
        activeWorldsFolder = new File("world/dimensions/minecraft/activedimensionswapworlds");
        if (!savedWorldsFolder.exists() || !activeWorldsFolder.exists()) {
            savedWorldsFolder.mkdirs();
            activeWorldsFolder.mkdirs();
        }
        WorldFileUtils.sanitizeWorldSaveFolder(savedWorldsFolder);
    }

    public static void preLoadSavedWorlds(Audience reporterPlayer) {
        WorldFileUtils.sanitizeWorldSaveFolder(savedWorldsFolder);
        WorldFileUtils.loadAllWorldsInFolder(savedWorldsFolder, reporterPlayer);
    }


    public void refreshWorldFolders() {
        yetToBeUsedWorlds.clear();
        for (File file : savedWorldsFolder.listFiles()) {
            if (file.isDirectory()) {
                yetToBeUsedWorlds.add(file.getName());
            }
        }
        Collections.shuffle(yetToBeUsedWorlds);
    }


    @Override
    public void onGameEnd() {
        if (getGame().getConfig().getDimensionSwapConfig().shouldSendPlayersToMainWorldAfterGameEnds()) {
            for (Player plr : this.getGame().getPlayers()) {
                plr.teleport(Bukkit.getWorld("world").getSpawnLocation());
            }
        }
    }

    @Override
    public void updateConfig() {
        this.setMinTicks(getGame().getConfig().getDimensionSwapConfig().getMinimumSecondsBeforeSwap() * 20);
        this.setMaxTicks(getGame().getConfig().getDimensionSwapConfig().getMaximumSecondsBeforeSwap() * 20);
        this.maxRounds = getGame().getConfig().getDimensionSwapConfig().getNumberOfSwaps();
    }

    @Override
    public void onGameStart() {
        WorldFileUtils.sanitizeWorldSaveFolder(savedWorldsFolder);
        updateConfig();
        super.onGameStart();
        this.currentRound = 0;
        refreshWorldFolders();
        try {
            for (World world : Bukkit.getWorlds()) {
                if (world.getName().contains("activedimensionswapworlds")) {
                    for (Player plr : world.getPlayers()) {
                        plr.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    }
                    Bukkit.unloadWorld(world, false);
                }
            }
            FileUtils.cleanDirectory(activeWorldsFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MinigamesWithFriends.getGamePlugin().getLogger().info("Starting game...");
        if (yetToBeUsedWorlds.isEmpty()) {
            getGame().broadcast(Component.text("No eligible dimensionswap worlds found! Disabling gamemode...").color(NamedTextColor.RED));
            getGame().broadcast(MiniMessage.miniMessage().deserialize(
                    "<red>Tell the server owner to add unzipped minecraft maps/worlds to </red>"
                    +"<yellow>/world/dimensions/minecraft/saveddimensiontpworlds</yellow><red>!</red>"
            ));
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), ()-> getGame().removeGamemode(DimensionSwapGamemode.class), 0);
        } else {
            shufflePlayers();
        }
    }


    @Override
    public void tick() {
        super.tick();
        getGame().addScoreboardContribution("§dDimension Switch in: §b" + getFormattedTimeRemaining());
        getGame().addScoreboardContribution("§dRounds Until Deathmatch: §b" + currentRound + "§b/§d" + maxRounds);
    }

    private void shufflePlayers() {
        MinigamesWithFriends.getGamePlugin().getLogger().info("Shuffling players...");
        if (currentRound >= maxRounds) {
            currentRound = 0;
            this.setTickGoal(2);
            getGame().startDeathMatch();
            return;
        }
        currentRound++;
        for (Player plr : this.getGame().getPlayers()) {

            plr.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false));
        }
        for (Player plr : this.getGame().getPlayers()) {
            String newWorld = yetToBeUsedWorlds.removeFirst();
            if (yetToBeUsedWorlds.isEmpty()) {
                refreshWorldFolders();
            }
            File originalWorld = new File(savedWorldsFolder.getPath() + "/" + newWorld);
            //paper changed this to look startings from the world/dimensions/minecraft/plugins/minigameswithfriends dir
            File newWorldFolder = new File(activeWorldsFolder + "/" + newWorld);
            try {
                FileUtils.copyDirectory(originalWorld, newWorldFolder);
            } catch (IOException e) {
                plr.sendMessage("§cError: IOException trying to copy world: " + newWorld + "!");
                e.printStackTrace();
            }
            MinigamesWithFriends.getGamePlugin().getLogger().info("name: " + newWorldFolder.getPath().replace("\\", "/"));
            String newWorldPath = new File("activedimensionswapworlds" + "/" + newWorld).getPath().replace("\\", "/");
            World world = Bukkit.createWorld(new WorldCreator(newWorldPath));
            world.setDifficulty(Difficulty.NORMAL);
            world.setGameRule(GameRules.BLOCK_DROPS, true);
            world.setGameRule(GameRules.FALL_DAMAGE, true);
            plr.setFallDistance(0);
            Location teleportLoc = BlockUtils.findSafeBlock(world.getSpawnLocation());
            BlockUtils.createSafePlatformIfNotExist(teleportLoc, 1);
            plr.teleport(teleportLoc);
        }
        for (Player plr : this.getGame().getPlayers()) {
            plr.removePotionEffect(PotionEffectType.RESISTANCE);
        }
    }

    @Override
    public void onTimeEventTrigger() {
        MinigamesWithFriends.getGamePlugin().getLogger().info("Time event triggered!");
        shufflePlayers();
    }

    @Override
    public String toString() {
        return "DimensionSwap";
    }
}
