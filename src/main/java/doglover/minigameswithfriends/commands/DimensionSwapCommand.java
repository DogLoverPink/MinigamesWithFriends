package doglover.minigameswithfriends.commands;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.configs.GamemodeConfig;
import doglover.minigameswithfriends.gamemodes.BlockShuffleGamemode;
import doglover.minigameswithfriends.gamemodes.DimensionSwapGamemode;
import doglover.minigameswithfriends.gamemodes.Gamemode;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DimensionSwapCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            commandSender.sendMessage("The options are:\n" +
                    "§b start\n" +
                    "§b config <config> <key> <value>\n");
            return true;
        }

        String minigameCommand = args[0];

        if (minigameCommand.equalsIgnoreCase("EnableGamemode")) {
            handleEnableGamemode(commandSender, args);
        }
        if (minigameCommand.equalsIgnoreCase("SafeLoc")) {
            Player plr = (Player) commandSender;
            plr.setFallDistance(0);
            plr.teleport(BlockUtils.findSafeBlock(plr.getLocation()));
        }
        if (minigameCommand.equalsIgnoreCase("preLoadSavedDimensionSwapWorlds")) {
            DimensionSwapGamemode.preLoadSavedWorlds((Player) commandSender);
        }
        if (minigameCommand.equalsIgnoreCase("launchToSpawn")) {
            Player plr = (Player) commandSender;
            PlayerUtils.launchPlayerToLoc(plr, plr.getWorld().getSpawnLocation());
        }
        if (minigameCommand.equalsIgnoreCase("DisableGamemode")) {
            handleDisableGamemode(commandSender, args);
        }
        if (minigameCommand.equalsIgnoreCase("ClearGamemodes")) {
            MinigamesWithFriends.getGame().clearGamemodes();
            commandSender.sendMessage("§aCleared all gamemodes.");
        }

        if (minigameCommand.equalsIgnoreCase("start")) {
            if (MinigamesWithFriends.getGame().getGamemodes().isEmpty()) {
                commandSender.sendMessage("§cNo gamemodes enabled. Add some by doing §e/minigames EnableGamemode <gamemodeName>.");
                return true;
            }
            MinigamesWithFriends.getGame().startGame();
            commandSender.sendMessage("§aGame started.");
        }
        if (minigameCommand.equalsIgnoreCase("fling")) {
            Player plr = (Player) commandSender;
            Game.launchPlayerSideways(plr, 10);
        }
        if (minigameCommand.equalsIgnoreCase("stop")) {
            MinigamesWithFriends.getGame().endGame();
            commandSender.sendMessage("§cGame stopped.");
        }
        if (minigameCommand.equalsIgnoreCase("blockshuffle")) {
            handleBlockShuffleCommand(commandSender, args);
        }
        if (minigameCommand.equalsIgnoreCase("config")) {
            if (args.length == 1) {
                commandSender.sendMessage("§cSpecify a gamemode and a config key.");
                commandSender.sendMessage("§eEx.: /dimensionSwap config dimensionSwap canVisitSameWorldTwice true");
                return true;
            }
            if (args.length == 2) {
                String key = args[1];
                handleConfig2Args(commandSender, key);
                return true;
            }
            if (args.length == 3) {
                handleFetchingConfigValue(commandSender, args[1], args[2]);
                return true;
            }
            if (args.length == 4) {
                handleSettingConfigValue(commandSender, args[1], args[2], args[3]);
            }
        }
        return false;
    }

    private static void handleDevCommand(CommandSender commandSender, String [] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("preLoadSavedDimensionSwapWorlds")) {
            DimensionSwapGamemode.preLoadSavedWorlds((Player) commandSender);
            commandSender.sendMessage("§aPreloaded saved worlds.");
        } else if (args[1].equalsIgnoreCase("launchToSpawn")) {
            Player plr = (Player) commandSender;
            PlayerUtils.launchPlayerToLoc(plr, plr.getWorld().getSpawnLocation());
        } else if (args[1].equalsIgnoreCase("SafeLoc")) {
            Player plr = (Player) commandSender;
            plr.setFallDistance(0);
            plr.teleport(BlockUtils.findSafeBlock(plr.getLocation()));
        } else if (args[1].equalsIgnoreCase("fling")) {
            Player plr = (Player) commandSender;
            Game.launchPlayerSideways(plr, 10);
        }
    }

    private static void handleBlockShuffleCommand(@NotNull CommandSender commandSender, @NotNull String @NotNull [] args) {
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
        }
        else if (args[1].equalsIgnoreCase("skip")) {
            if (!MinigamesWithFriends.getGame().isRunning() || !MinigamesWithFriends.getGame().isGamemodeActive(BlockShuffleGamemode.class)) {
                return;
            }
            MinigamesWithFriends.getGame().getGamemode(BlockShuffleGamemode.class).skip();
        }
    }

    private void handleDisableGamemode(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            String moduleName = args[1];
            if (!Gamemode.isValidGamemode(moduleName)) {
                commandSender.sendMessage("§cInvalid gamemode name.");
                commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
                return;
            }
            MinigamesWithFriends.getGame().removeGamemode(Gamemode.getGamemodeFromName(moduleName).getClass());
            commandSender.sendMessage("§aGamemode " + moduleName + " disabled.");
        } else {
            commandSender.sendMessage("§cPlease specify a module name.");
            commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
        }
    }

    private void handleEnableGamemode(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            String moduleName = args[1];
            if (!Gamemode.isValidGamemode(moduleName)) {
                commandSender.sendMessage("§cInvalid gamemode name.");
                commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
                return;
            }
            if (MinigamesWithFriends.getGame().isGamemodeActive(Gamemode.getGamemodeFromName(moduleName).getClass())) {
                commandSender.sendMessage("§cGamemode " + moduleName + " is already enabled.");
                return;
            }
            MinigamesWithFriends.getGame().addGamemode(Gamemode.getGamemodeFromName(moduleName));
            commandSender.sendMessage("§aGamemode " + moduleName + " enabled.");
        } else {
            commandSender.sendMessage("§cPlease specify a module name.");
            commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
        }
    }

    private void handleSettingConfigValue(CommandSender commandSender, String configName, String configKey, String value) {
        GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        Class<?> type = conf.getConfigValues().get(configKey);
        if (type == null) {
            commandSender.sendMessage("§cConfig key not found.");
            return;
        }
        boolean wasValid = conf.validateAndSetValue(configKey, value);
        if (!wasValid) {
            commandSender.sendMessage("§cInvalid value type. Expected: " + type.getSimpleName());
            return;
        }
        commandSender.sendMessage("§aConfig: " + configKey + " set to " + value);
    }

    private void handleFetchingConfigValue(CommandSender commandSender, String configName, String configKey) {
        GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        String value = conf.getString(configKey);
        if (value == null) {
            commandSender.sendMessage("§cConfig key not found.");
            return;
        }
        commandSender.sendMessage("§aConfig: " + configKey + " = " + value);
    }

    private void sendValidGamemodeNames(CommandSender commandSender) {
        commandSender.sendMessage("""
                §cGamemode not found,valid options are:
                §dmainGame
                §ddimensionSwap""");
    }

    private void handleConfig2Args(CommandSender commandSender, String configName) {
        GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        commandSender.sendMessage("§eValid config values:");
        for (String configKey : conf.getConfigValues().keySet()) {
            String type = conf.getConfigValues().get(configKey).getSimpleName();
            String value = conf.getString(configKey);
            commandSender.sendMessage("§d" + configKey + " = " +value+ " §e: " + type);
        }
    }
}
