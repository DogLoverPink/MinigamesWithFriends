package doglover.dimensionSwap;

import doglover.dimensionSwap.configs.GamemodeConfig;
import doglover.dimensionSwap.gamemodes.DimensionSwapGamemode;
import org.bukkit.Bukkit;
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

        if (args[0].equalsIgnoreCase("addPlayer")) {
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
        if (args[0].equalsIgnoreCase("start")) {
            DimensionSwap.getGame().clearGamemodes();
            DimensionSwapGamemode gamemode = new DimensionSwapGamemode();
            DimensionSwap.getGame().addGamemode(gamemode);
            DimensionSwap.getGame().startGame();
            commandSender.sendMessage("§aGame started.");
        }
        if (args[0].equalsIgnoreCase("fling")) {
            Player plr = (Player) commandSender;
            Game.launchPlayerSideways(plr, 10);
        }
        if (args[0].equalsIgnoreCase("stop")) {
            DimensionSwap.getGame().endGame();
            commandSender.sendMessage("§cGame stopped.");
        }
        if (args[0].equalsIgnoreCase("config")) {
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
                String key = args[1];
                if (key.equalsIgnoreCase("save")) {
                    ((DimensionSwapGamemode) DimensionSwap.getGame().getGamemodes().get(0)).getConfig().saveConfig();
                    return true;
                }

                if (key.equalsIgnoreCase("list")) {
                    commandSender.sendMessage("§aConfig list:\n§d" +
                            "canVisitSameWorldTwice\n" +
                            "minimumSecondsBeforeSwap\n" +
                            "maximumSecondsBeforeSwap\n" +
                            "numbersOfSwaps");
                    return true;
                }
                String value = ((DimensionSwapGamemode) DimensionSwap.getGame().getGamemodes().get(0)).getConfig().getConfigValue(key);
                commandSender.sendMessage("§aConfig: " + key + " = " + value);
            }
        }
            return false;
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
        Object convertedValue = convertValue(value, type);
        if (convertedValue == null) {
            commandSender.sendMessage("§cInvalid value type.");
            return;
        }
        conf.set(configKey, convertedValue);
        commandSender.sendMessage("§aConfig: " + configKey + " set to " + convertedValue);
    }

    private void handleFetchingConfigValue(CommandSender commandSender, String configName, String configKey) {
        GamemodeConfig conf = DimensionSwap.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        Object value = conf.getConfigValues().get(configKey);
        if (value == null) {
            commandSender.sendMessage("§cConfig key not found.");
            return;
        }
        commandSender.sendMessage("§aConfig: " + configKey + " = " + value);
    }

    private void sendValidGamemodeNames(CommandSender commandSender) {
        commandSender.sendMessage("""
                §cGamemode not found,valid options are:
                §ddimensionSwap
                placeholder""");
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
            commandSender.sendMessage("§d" + configKey + "§e: " + type);
        }
    }
}
