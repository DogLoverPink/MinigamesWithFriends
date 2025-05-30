package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.WorldFileUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DimensionSwapGamemode extends TimeEventBasedGamemode {

    static File savedWorldsFolder;
    static File activeWorldsFolder;
    List<String> yetToBeUsedWorlds = new ArrayList<>();
    private int maxRounds;
    private int currentRound = 0;


    public static void initialize() {
        savedWorldsFolder = new File(MinigamesWithFriends.getGamePlugin().getDataFolder() + "/" + "savedDimensionTPWorlds");
        activeWorldsFolder = new File(MinigamesWithFriends.getGamePlugin().getDataFolder() + "/" + "activeDimensionTPWorlds");
        if (!savedWorldsFolder.exists() || !activeWorldsFolder.exists()) {
            savedWorldsFolder.mkdirs();
            activeWorldsFolder.mkdirs();
        }
        WorldFileUtils.sanitizeWorldSaveFolder(savedWorldsFolder);
    }

    public static void preLoadSavedWorlds(Player player) {
        WorldFileUtils.sanitizeWorldSaveFolder(savedWorldsFolder);
        WorldFileUtils.loadAllWorldsInFolder(savedWorldsFolder, player);
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
    public void onGameStart() {
        WorldFileUtils.sanitizeWorldSaveFolder(savedWorldsFolder);
        this.setMinTicks(getGame().getConfig().getDimensionSwapConfig().getMinimumSecondsBeforeSwap() * 20);
        this.setMaxTicks(getGame().getConfig().getDimensionSwapConfig().getMaximumSecondsBeforeSwap() * 20);
        super.onGameStart();
        this.maxRounds = getGame().getConfig().getDimensionSwapConfig().getNumberOfSwaps();
        this.currentRound = 0;
        refreshWorldFolders();
        try {
            for (World world : Bukkit.getWorlds()) {
                if (world.getName().contains("activeDimensionTPWorlds")) {
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
        shufflePlayers();
    }

    @Override
    public void tick() {
        super.tick();
        getGame().addScoreboardContributution("§bDimension Switch in: §d" + getFormattedTimeRemaining());
        getGame().addScoreboardContributution("§bDimension Round: §d" + currentRound + "§b/§d" + maxRounds);
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
            File newWorldFolder = new File(activeWorldsFolder.getPath() + "/" + newWorld);
            try {
                FileUtils.copyDirectory(originalWorld, newWorldFolder);
            } catch (IOException e) {
                plr.sendMessage("§cError: IOException trying to copy world: " + newWorld + "!");
                e.printStackTrace();
            }
            MinigamesWithFriends.getGamePlugin().getLogger().info("name: " + newWorldFolder.getPath().replace("\\", "/"));
            World world = Bukkit.createWorld(new WorldCreator(newWorldFolder.getPath().replace("\\", "/")));
            world.setDifficulty(Difficulty.NORMAL);
            world.setGameRule(GameRule.DO_TILE_DROPS, true);
            world.setGameRule(GameRule.FALL_DAMAGE, true);
            plr.setFallDistance(0);
            plr.teleport(BlockUtils.findSafeBlock(world.getSpawnLocation()));
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
