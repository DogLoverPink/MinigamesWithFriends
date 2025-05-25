package doglover.dimensionSwap.commands;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.Game;
import doglover.dimensionSwap.configs.GamemodeConfig;
import doglover.dimensionSwap.gamemodes.BlockShuffleGamemode;
import doglover.dimensionSwap.gamemodes.Gamemode;
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
                    "§b addPlayer <player>\n" +
                    "§b start\n" +
                    "§b config <key> <value>\n" +
                    "§b config list");
            return true;
        }

        String minigameCommand = args[0];

        if (minigameCommand.equalsIgnoreCase("EnableGamemode")) {
            handleEnableGamemode(commandSender, args);
        }
        if (minigameCommand.equalsIgnoreCase("DisableGamemode")) {
            handleDisableGamemode(commandSender, args);
        }
        if (minigameCommand.equalsIgnoreCase("ClearGamemodes")) {
            DimensionSwap.getGame().clearGamemodes();
            commandSender.sendMessage("§aCleared all gamemodes.");
        }

        if (minigameCommand.equalsIgnoreCase("AddPlayer")) {
            if (args.length == 2) {
                String playerName = args[1];
                DimensionSwap.getGame().getPlayers().add(DimensionSwap.getGamePlugin().getServer().getPlayer(playerName));
                commandSender.sendMessage("§aPlayer " + playerName + " added to the game.");
                return true;
            } else {
                commandSender.sendMessage("§cPlease specify a player name.");
                return true;
            }
        }
        if (minigameCommand.equalsIgnoreCase("start")) {
            if (DimensionSwap.getGame().getGamemodes().isEmpty()) {
                commandSender.sendMessage("§cNo gamemodes enabled. Add some by doing §e/minigames EnableGamemode <gamemodeName>.");
                return true;
            }
            DimensionSwap.getGame().startGame();
            commandSender.sendMessage("§aGame started.");
        }
        if (minigameCommand.equalsIgnoreCase("fling")) {
            Player plr = (Player) commandSender;
            Game.launchPlayerSideways(plr, 10);
        }
        if (minigameCommand.equalsIgnoreCase("stop")) {
            DimensionSwap.getGame().endGame();
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
            if (!DimensionSwap.getGame().isRunning() || !DimensionSwap.getGame().isGamemodeActive(BlockShuffleGamemode.class)) {
                return;
            }
            DimensionSwap.getGame().getGamemode(BlockShuffleGamemode.class).skip();
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
            DimensionSwap.getGame().removeGamemode(Gamemode.getGamemodeFromName(moduleName).getClass());
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
            if (DimensionSwap.getGame().isGamemodeActive(Gamemode.getGamemodeFromName(moduleName).getClass())) {
                commandSender.sendMessage("§cGamemode " + moduleName + " is already enabled.");
                return;
            }
            DimensionSwap.getGame().addGamemode(Gamemode.getGamemodeFromName(moduleName));
            commandSender.sendMessage("§aGamemode " + moduleName + " enabled.");
        } else {
            commandSender.sendMessage("§cPlease specify a module name.");
            commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
        }
    }

    private void handleSettingConfigValue(CommandSender commandSender, String configName, String configKey, String value) {
        GamemodeConfig conf = DimensionSwap.getGame().getConfig().getGamemodeConfigFromName(configName);
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
        GamemodeConfig conf = DimensionSwap.getGame().getConfig().getGamemodeConfigFromName(configName);
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
        GamemodeConfig conf = DimensionSwap.getGame().getConfig().getGamemodeConfigFromName(configName);
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
