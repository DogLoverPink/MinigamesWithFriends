package doglover.minigameswithfriends.commands;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.configs.GameModuleConfig;
import doglover.minigameswithfriends.gamemodes.GameModule;
import doglover.minigameswithfriends.utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
                "EnableModifier",
                BuiltInCommandDefinitions::handleEnableModifierCommand,
                BuiltInCommandDefinitions::handleEnableModifierCompletions);
        CommandHandler.registerCommand(
                "DisableModifier",
                BuiltInCommandDefinitions::handleDisableModifierCommand,
                BuiltInCommandDefinitions::handleDisableModifierCompletions);
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
                "ConfigMainGame",
                BuiltInCommandDefinitions::handleConfigMainGameCommand,
                BuiltInCommandDefinitions::handleConfigMainGameCompletions);
        CommandHandler.registerCommand(
                "ConfigGamemode",
                BuiltInCommandDefinitions::handleConfigGamemodeCommand,
                BuiltInCommandDefinitions::handleConfigGamemodeCompletions);
        CommandHandler.registerCommand(
                "ConfigModifier",
                BuiltInCommandDefinitions::handleConfigModifierCommand,
                BuiltInCommandDefinitions::handleConfigModifierCompletions);
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
        handleEnableModuleCommand(commandSender, args, GameModule.Type.GAMEMODE, "Gamemode");
    }

    private static void handleEnableModifierCommand(CommandSender commandSender, String[] args) {
        handleEnableModuleCommand(commandSender, args, GameModule.Type.MODIFIER, "Modifier");
    }

    private static void handleEnableModuleCommand(CommandSender commandSender, String[] args, GameModule.Type type, String label) {
        if (args.length == 2) {
            String moduleName = args[1];
            if (!GameModule.isValidModule(moduleName, type)) {
                commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Invalid " + label.toLowerCase() + " name."));
                commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>Valid options are: <aqua>" + GameModule.getModuleListString(type)));
                return;
            }
            if (MinigamesWithFriends.getGame().isModuleActive(GameModule.getModuleFromName(moduleName).getClass())) {
                commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>" + label + " " + moduleName + " is already enabled."));
                return;
            }
            MinigamesWithFriends.getGame().addModule(GameModule.getModuleFromName(moduleName));
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>" + label + " " + moduleName + " enabled."));
        } else {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Please specify a " + label.toLowerCase() + " name."));
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>Valid options are: <aqua>" + GameModule.getModuleListString(type)));
        }
    }

    private static List<String> handleEnableGamemodeCompletions(String[] args) {
        return enableCompletions(args, GameModule.Type.GAMEMODE);
    }

    private static List<String> handleEnableModifierCompletions(String[] args) {
        return enableCompletions(args, GameModule.Type.MODIFIER);
    }

    private static List<String> enableCompletions(String[] args, GameModule.Type type) {
        String input = args.length > 1 ? args[1] : "";
        Game game = MinigamesWithFriends.getGame();
        List<String> names = GameModule.getModuleClassList(type).stream()
                .filter(moduleClass -> !game.isModuleActive(moduleClass))
                .map(GameModule::getModuleNameFromClass)
                .toList();
        return filterByStartsWith(names, input);
    }


    private static void handleClearGamemodesCommand(CommandSender commandSender, String[] args) {
        MinigamesWithFriends.getGame().clearModules();
        commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Cleared all gamemodes."));
    }

    private static void handleStartCommand(CommandSender commandSender, String[] args) {
        if (!canStart(commandSender)) {
            return;
        }
        MinigamesWithFriends.getGame().startGame();
        commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Game started."));
    }

    private static void handleStopCommand(CommandSender commandSender, String[] args) {
        if (!MinigamesWithFriends.getGame().isRunning()) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>No game currently in progress!"));
            return;
        }
        MinigamesWithFriends.getGame().endGame();
        commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Game stopped."));
    }

    private static boolean canStart(CommandSender sender) {
        if (MinigamesWithFriends.getGame().isRunning()) {
            sender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Game already started!"));
            return false;
        }
        if (MinigamesWithFriends.getGame().getModules().isEmpty()) {
            sender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>No gamemodes enabled. Add some by doing <yellow>/minigames EnableGamemode \\<gamemodeName\\>."));
            return false;
        }
        if (MinigamesWithFriends.getGame().getSpectators().size() == Bukkit.getOnlinePlayers().size()) {
            sender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>You need some non-spectator players to start the game you silly goose!"));
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
        Component message = TextUtils.MINI_MESSAGE.deserialize(
                "<yellow>Wiki Link: <click:open_url:'https://github.com/DogLoverPink/MinigamesWithFriends/wiki'><hover:show_text:'<aqua>Wiki Link</aqua>'><aqua>HERE</aqua></hover></click></yellow>\n" +
                        "<yellow>Discord Link: <click:open_url:'https://discord.gg/KcGdBFUVZD'><hover:show_text:'<aqua>Join for fast support!</aqua>'><aqua>HERE</aqua></hover></click></yellow>"
        );
        commandSender.sendMessage(message);
    }

    private static void handleDisableGamemodeCommand(CommandSender commandSender, String[] args) {
        handleDisableModuleCommand(commandSender, args, GameModule.Type.GAMEMODE, "Gamemode");
    }

    private static void handleDisableModifierCommand(CommandSender commandSender, String[] args) {
        handleDisableModuleCommand(commandSender, args, GameModule.Type.MODIFIER, "Modifier");
    }

    private static void handleDisableModuleCommand(CommandSender commandSender, String[] args, GameModule.Type type, String label) {
        if (args.length == 2) {
            String moduleName = args[1];
            if (!GameModule.isValidModule(moduleName, type)) {
                commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Invalid " + label.toLowerCase() + " name."));
                commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>Valid options are: <aqua>" + GameModule.getModuleListString(type)));
                return;
            }
            MinigamesWithFriends.getGame().removeModule(GameModule.getModuleFromName(moduleName).getClass());
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>" + label + " " + moduleName + " disabled."));
        } else {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Please specify a " + label.toLowerCase() + " name."));
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>Valid options are: <aqua>" + GameModule.getModuleListString(type)));
        }
    }

    private static List<String> handleDisableGamemodeCompletions(String[] args) {
        return disableCompletions(args, GameModule.Type.GAMEMODE);
    }

    private static List<String> handleDisableModifierCompletions(String[] args) {
        return disableCompletions(args, GameModule.Type.MODIFIER);
    }

    private static List<String> disableCompletions(String[] args, GameModule.Type type) {
        String input = args.length > 1 ? args[1] : "";
        List<String> names = MinigamesWithFriends.getGame().getModules().stream()
                .filter(module -> module.getType() == type)
                .map(module -> GameModule.getModuleNameFromClass(module.getClass()))
                .toList();
        return filterByStartsWith(names, input);
    }

    private static void handleConfigGamemodeCommand(CommandSender commandSender, String[] args) {
        handleConfigModuleCommand(commandSender, args, GameModule.Type.GAMEMODE, "gamemode");
    }

    private static void handleConfigModifierCommand(CommandSender commandSender, String[] args) {
        handleConfigModuleCommand(commandSender, args, GameModule.Type.MODIFIER, "modifier");
    }

    private static void handleConfigModuleCommand(CommandSender commandSender, String[] args, GameModule.Type type, String label) {
        if (args.length == 1) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Specify a " + label + " and a config key."));
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>Valid " + label + "s are: <aqua>" + GameModule.getModuleListString(type)));
            return;
        }
        GameModuleConfig conf = configOfType(args[1], type);
        if (conf == null) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>No " + label + " named <yellow>" + TextUtils.MINI_MESSAGE.escapeTags(args[1])));
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>Valid " + label + "s are: <aqua>" + GameModule.getModuleListString(type)));
            return;
        }
        if (args.length == 2) {
            listConfigValues(commandSender, conf);
        } else if (args.length == 3) {
            fetchConfigValue(commandSender, conf, args[2]);
        } else if (args.length == 4) {
            setConfigValue(commandSender, conf, args[2], args[3]);
        }
    }

    private static void handleConfigMainGameCommand(CommandSender commandSender, String[] args) {
        GameModuleConfig conf = MinigamesWithFriends.getGame().getConfig();
        if (args.length == 1) {
            listConfigValues(commandSender, conf);
        } else if (args.length == 2) {
            fetchConfigValue(commandSender, conf, args[1]);
        } else if (args.length == 3) {
            setConfigValue(commandSender, conf, args[1], args[2]);
        }
    }

    private static GameModuleConfig configOfType(String name, GameModule.Type type) {
        return GameModule.isValidModule(name, type) ? GameModule.getConfigFromName(name) : null;
    }

    private static List<String> handleConfigGamemodeCompletions(String[] args) {
        return configModuleCompletions(args, GameModule.Type.GAMEMODE);
    }

    private static List<String> handleConfigModifierCompletions(String[] args) {
        return configModuleCompletions(args, GameModule.Type.MODIFIER);
    }

    private static List<String> configModuleCompletions(String[] args, GameModule.Type type) {
        if (args.length == 2) {
            return filterByStartsWith(GameModule.getModuleList(type), args[1]);
        }
        if (args.length == 3) {
            return configKeyCompletions(configOfType(args[1], type), args[2]);
        }
        if (args.length == 4) {
            return configValueCompletions(configOfType(args[1], type), args[2], args[3]);
        }
        return List.of();
    }

    private static List<String> handleConfigMainGameCompletions(String[] args) {
        GameModuleConfig conf = MinigamesWithFriends.getGame().getConfig();
        if (args.length == 2) {
            return configKeyCompletions(conf, args[1]);
        }
        if (args.length == 3) {
            return configValueCompletions(conf, args[1], args[2]);
        }
        return List.of();
    }

    private static List<String> configKeyCompletions(GameModuleConfig conf, String input) {
        if (conf == null) {
            return List.of();
        }
        return filterByStartsWith(new ArrayList<>(conf.getConfigValues().keySet()), input);
    }

    private static List<String> configValueCompletions(GameModuleConfig conf, String key, String input) {
        if (conf == null) {
            return List.of();
        }
        Class<?> type = conf.getConfigValues().get(key);
        if (type == null) {
            return List.of();
        } else if (type.equals(Boolean.class)) {
            return filterByStartsWith(List.of("true", "false"), input);
        } else if (type.equals(GameModuleConfig.StringEnum.class)) {
            return filterByStartsWith(conf.getCompletionsForStringEnum(key), input);
        }
        return List.of();
    }

    private static void listConfigValues(CommandSender commandSender, GameModuleConfig conf) {
        commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>Valid config values:"));
        for (String configKey : conf.getConfigValues().keySet()) {
            String type = conf.getConfigValues().get(configKey).getSimpleName();
            String value = conf.getString(configKey);
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<light_purple>" + configKey + " = " + TextUtils.MINI_MESSAGE.escapeTags(value) + " <yellow>: " + type));
        }
    }

    private static void fetchConfigValue(CommandSender commandSender, GameModuleConfig conf, String configKey) {
        String value = conf.getString(configKey);
        if (value == null) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Config key not found."));
            return;
        }
        commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Config: " + configKey + " = " + TextUtils.MINI_MESSAGE.escapeTags(value)));
    }

    private static void setConfigValue(CommandSender commandSender, GameModuleConfig conf, String configKey, String value) {
        Class<?> type = conf.getConfigValues().get(configKey);
        if (type == null) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Config key not found."));
            return;
        }
        boolean wasValid = conf.validateAndSetValue(configKey, value);
        if (!wasValid) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Invalid value type. Expected: " + type.getSimpleName()));
            return;
        }
        commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Config: " + configKey + " set to " + TextUtils.MINI_MESSAGE.escapeTags(value)));
        MinigamesWithFriends.getGame().updateConfig();
    }

    private static void handleAddSpectatorCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Please specify a player!"));
            return;
        }
        Player plr = Bukkit.getPlayer(args[1]);
        if (plr == null) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Please specify a valid player!"));
            return;
        }
        if (MinigamesWithFriends.getGame().getSpectators().contains(plr)) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>That player is already a spectator!"));
            return;
        }
        MinigamesWithFriends.getGame().addSpectator(plr);
        if (!commandSender.equals(plr)) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Made <aqua>" + plr.getName() + "<green> a spectator!"));
        }
        if (!MinigamesWithFriends.getGame().isRunning()) {
            plr.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>You have been set to spectate the next game!"));
        }
    }

    private static List<String> handleAddSpectatorCompletions(String[] args) {
        Set<Player> allPlayers = new HashSet<>(Bukkit.getOnlinePlayers());
        allPlayers.removeAll(MinigamesWithFriends.getGame().getSpectators());
        return allPlayers.stream().map(Player::getName).toList();
    }

    private static void handleRemoveSpectatorCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Please specify a player!"));
            return;
        }
        Player plr = Bukkit.getPlayer(args[1]);
        if (plr == null) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Please specify a valid player!"));
            return;
        }
        if (!MinigamesWithFriends.getGame().getSpectators().contains(plr)) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>That player is not a spectator!"));
            return;
        }
        MinigamesWithFriends.getGame().removeSpectator(plr);
        commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Made <aqua>" + plr.getName() + "<green> no longer a spectator!"));
    }

    private static List<String> handleRemoveSpectatorCompletions(String[] args) {
        return MinigamesWithFriends.getGame().getSpectators().stream().map(Player::getName).toList();
    }

    public static List<String> filterByStartsWith(List<String> list, String filter) {
        return list.stream().filter(s -> s.toLowerCase().startsWith(filter.toLowerCase())).toList();
    }
}