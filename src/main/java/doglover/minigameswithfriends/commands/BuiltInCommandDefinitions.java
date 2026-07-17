package doglover.minigameswithfriends.commands;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.configs.GamemodeConfig;
import doglover.minigameswithfriends.gamemodes.Gamemode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuiltInCommandDefinitions {
    public static void registerCommands() {
        CommandHandler.registerCommand(
                "EnableGamemode",
                BuiltInCommandDefinitions::handleEnableGamemodeCommand,
                BuiltInCommandDefinitions::handleEnableGamemodeCompletions);
        CommandHandler.registerCommand(
                "DisableGamemode",
                BuiltInCommandDefinitions::handleDisableGamemodeCommand,
                BuiltInCommandDefinitions::handleDisableGamemodeCompletions);
        CommandHandler.registerCommand(
                "help",
                BuiltInCommandDefinitions::handleHelpCommand);
        CommandHandler.registerCommand(
                "ClearGamemodes",
                BuiltInCommandDefinitions::handleClearGamemodesCommand);
        CommandHandler.registerCommand(
                "start",
                BuiltInCommandDefinitions::handleStartCommand);
        CommandHandler.registerCommand(
                "stop",
                BuiltInCommandDefinitions::handleStopCommand);
        CommandHandler.registerCommand(
                "pause",
                BuiltInCommandDefinitions::handlePauseCommand);
        CommandHandler.registerCommand(
                "unpause",
                BuiltInCommandDefinitions::handleUnpauseCommand);
        CommandHandler.registerCommand(
                "config",
                BuiltInCommandDefinitions::handleConfigCommand,
                BuiltInCommandDefinitions::handleConfigCompletions);
        CommandHandler.registerCommand(
                "AddSpectator",
                BuiltInCommandDefinitions::handleAddSpectatorCommand,
                BuiltInCommandDefinitions::handleAddSpectatorCompletions);
        CommandHandler.registerCommand(
                "RemoveSpectator",
                BuiltInCommandDefinitions::handleRemoveSpectatorCommand,
                BuiltInCommandDefinitions::handleRemoveSpectatorCompletions);
    }

    private static void handleEnableGamemodeCommand(CommandSender commandSender, String[] args) {
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

    private static List<String> handleEnableGamemodeCompletions(String[] args) {
        String input = args.length > 1 ? args[1] : "";
        Game game = MinigamesWithFriends.getGame();
        List<String> gamemodes = Gamemode.getGamemodeClassList().stream()
                .filter(gamemode -> !game.isGamemodeActive(gamemode))
                .map(Gamemode::getGamemodeNameFromClass)
                .toList();
        return filterByStartsWith(gamemodes, input);
    }


    private static void handleClearGamemodesCommand(CommandSender commandSender, String[] args) {
        MinigamesWithFriends.getGame().clearGamemodes();
        commandSender.sendMessage("§aCleared all gamemodes.");
    }

    private static void handleStartCommand(CommandSender commandSender, String[] args) {
        if (!canStart(commandSender)) {
            return;
        }
        MinigamesWithFriends.getGame().startGame();
        commandSender.sendMessage("§aGame started.");
    }

    private static void handleStopCommand(CommandSender commandSender, String[] args) {
        if (!MinigamesWithFriends.getGame().isRunning()) {
            commandSender.sendMessage("§cNo game currently in progress!");
            return;
        }
        MinigamesWithFriends.getGame().endGame();
        commandSender.sendMessage("§cGame stopped.");
    }

    private static boolean canStart(CommandSender sender) {
        if (MinigamesWithFriends.getGame().isRunning()) {
            sender.sendMessage("§cGame already started!");
            return false;
        }
        if (MinigamesWithFriends.getGame().getGamemodes().isEmpty()) {
            sender.sendMessage("§cNo gamemodes enabled. Add some by doing §e/minigames EnableGamemode <gamemodeName>.");
            return false;
        }
        if (MinigamesWithFriends.getGame().getSpectators().size() == Bukkit.getOnlinePlayers().size()) {
            sender.sendMessage("§cYou need some non-spectator players to start the game you silly goose!");
            return false;
        }
        return true;
    }

    private static void handlePauseCommand(CommandSender commandSender, String[] args) {
        Game game = MinigamesWithFriends.getGame();
        if (game.isPaused()) {
            commandSender.sendMessage(Component.text("Already paused!").color(NamedTextColor.RED));
            return;
        }
        if (!game.isRunning()) {
            commandSender.sendMessage(Component.text("Game not in progress").color(NamedTextColor.RED));
            return;
        }
        game.setPaused(true);
        game.getPlayers().forEach(player -> {
            player.sendMessage(Component.text("Game Paused!").color(NamedTextColor.GREEN));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);
        });
        commandSender.sendMessage(Component.text("Timers have been paused, but note that pausing is not fully supported by every part of this plugin").color(NamedTextColor.YELLOW));
    }

    private static void handleUnpauseCommand(CommandSender commandSender, String[] args) {
        Game game = MinigamesWithFriends.getGame();
        if (!game.isRunning()) {
            commandSender.sendMessage(Component.text("Game not in progress").color(NamedTextColor.RED));
            return;
        }
        if (!game.isPaused()) {
            commandSender.sendMessage(Component.text("Game not currently paused!").color(NamedTextColor.RED));
            return;
        }
        game.setPaused(false);
        game.getPlayers().forEach(player -> {
            player.sendMessage(Component.text("Game Unpaused!").color(NamedTextColor.GREEN));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);
        });
    }

    private static void handleHelpCommand(CommandSender commandSender, String[] args) {
        Component message = MiniMessage.miniMessage().deserialize(
                "<yellow>Wiki Link: <click:open_url:'https://github.com/DogLoverPink/MinigamesWithFriends/wiki'><hover:show_text:'<aqua>Wiki Link</aqua>'><aqua>HERE</aqua></hover></click></yellow>\n" +
                        "<yellow>Discord Link: <click:open_url:'https://discord.gg/KcGdBFUVZD'><hover:show_text:'<aqua>Join for fast support!</aqua>'><aqua>HERE</aqua></hover></click></yellow>"
        );
        commandSender.sendMessage(message);
    }

    private static void handleDisableGamemodeCommand(CommandSender commandSender, String[] args) {
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

    private static List<String> handleDisableGamemodeCompletions(String[] args) {
        return filterByStartsWith(MinigamesWithFriends.getGame().getGamemodesAsString(), args[1]);
    }

    private static void handleConfigCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cSpecify a gamemode and a config key.");
            commandSender.sendMessage("§eEx.: /dimensionSwap config dimensionSwap canVisitSameWorldTwice true");
            return;
        }
        if (args.length == 2) {
            String key = args[1];
            handleConfig2Args(commandSender, key);
            return;
        }
        if (args.length == 3) {
            handleFetchingConfigValue(commandSender, args[1], args[2]);
            return;
        }
        if (args.length == 4) {
            handleSettingConfigValue(commandSender, args[1], args[2], args[3]);
        }
    }

    private static List<String> handleConfigCompletions(String[] args) {
        if (args.length == 2) {
            List<String> gamemodes = new ArrayList<>(Gamemode.getGamemodeList());
            gamemodes.add("mainGame");
            return filterByStartsWith(gamemodes, args[1]);
        }
        if (args.length == 3) {
            String configName = args[1];
            GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
            if (conf == null) {
                return List.of();
            }
            return conf.getConfigValues().keySet().stream().toList();
        }
        if (args.length == 4) {
            String configName = args[1];
            String key = args[2];
            GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
            if (conf == null) {
                return List.of();
            }
            Class<?> type = conf.getConfigValues().get(key);
            if (type == null) {
                return List.of();
            }
            if (type.equals(Boolean.class)) {
                return filterByStartsWith(List.of("true", "false"), args[3]);
            } else {
                return List.of();
            }
        }
        return null;
    }

    private static void handleSettingConfigValue(CommandSender commandSender, String configName, String configKey, String value) {
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
        MinigamesWithFriends.getGame().updateConfig();
    }

    private static void handleFetchingConfigValue(CommandSender commandSender, String configName, String configKey) {
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

    private static void sendValidGamemodeNames(CommandSender commandSender) {
        commandSender.sendMessage("""
                §cGamemode not found,valid options are:
                §dmainGame
                §ddimensionSwap""");
    }

    private static void handleConfig2Args(CommandSender commandSender, String configName) {
        GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        commandSender.sendMessage("§eValid config values:");
        for (String configKey : conf.getConfigValues().keySet()) {
            String type = conf.getConfigValues().get(configKey).getSimpleName();
            String value = conf.getString(configKey);
            commandSender.sendMessage("§d" + configKey + " = " + value + " §e: " + type);
        }
    }

    private static void handleAddSpectatorCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a player!");
            return;
        }
        Player plr = Bukkit.getPlayer(args[1]);
        if (plr == null) {
            commandSender.sendMessage("§cPlease specify a valid player!");
            return;
        }
        if (MinigamesWithFriends.getGame().getSpectators().contains(plr)) {
            commandSender.sendMessage("§cThat player is already a spectator!");
            return;
        }
        MinigamesWithFriends.getGame().addSpectator(plr);
        if (!commandSender.equals(plr)) {
            commandSender.sendMessage("§aMade §b" + plr.getName() + "§a a spectator!");
        }
        if (!MinigamesWithFriends.getGame().isRunning()) {
            plr.sendMessage("§aYou have been set to spectate the next game!");
        }
    }

    private static List<String> handleAddSpectatorCompletions(String[] args) {
        Set<Player> allPlayers = new HashSet<>(Bukkit.getOnlinePlayers());
        allPlayers.removeAll(MinigamesWithFriends.getGame().getSpectators());
        return allPlayers.stream().map(Player::getName).toList();
    }

    private static void handleRemoveSpectatorCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a player!");
            return;
        }
        Player plr = Bukkit.getPlayer(args[1]);
        if (plr == null) {
            commandSender.sendMessage("§cPlease specify a valid player!");
            return;
        }
        if (!MinigamesWithFriends.getGame().getSpectators().contains(plr)) {
            commandSender.sendMessage("§cThat player is not a spectator!");
            return;
        }
        MinigamesWithFriends.getGame().removeSpectator(plr);
        commandSender.sendMessage("§aMade §b" + plr.getName() + "§a no longer a spectator!");
    }

    private static List<String> handleRemoveSpectatorCompletions(String[] args) {
        return MinigamesWithFriends.getGame().getSpectators().stream().map(Player::getName).toList();
    }

    public static List<String> filterByStartsWith(List<String> list, String filter) {
        return list.stream().filter(s -> s.toLowerCase().startsWith(filter.toLowerCase())).toList();
    }
}