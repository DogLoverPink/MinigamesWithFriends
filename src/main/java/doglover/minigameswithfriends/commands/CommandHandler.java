package doglover.minigameswithfriends.commands;

import doglover.minigameswithfriends.utils.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static doglover.minigameswithfriends.commands.BuiltInCommandDefinitions.filterByStartsWith;

public class CommandHandler {

    private static final Map<String, BiConsumer<CommandSender, String[]>> commandMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, Function<String[], List<String>>> tabCompletionsMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static void registerCommand(String commandName, BiConsumer<CommandSender, String[]> command) {
        commandMap.put(commandName, command);
    }

    public static void registerCommand(String commandName, BiConsumer<CommandSender, String[]> command, Function<String[], List<String>> tabCompletions) {
        registerCommand(commandName, command);
        tabCompletionsMap.put(commandName, tabCompletions);
    }


    public static List<String> getTabCompletions(CommandSender sender, String[] args) {
        String commandName = args[0];
        if (args.length == 1) {
            return filterByStartsWith(new ArrayList<>(commandMap.keySet()), args[0]);
        }
        if (tabCompletionsMap.containsKey(commandName)) {
            return tabCompletionsMap.get(commandName).apply(args);
        }
        return null;
    }

    public static void handleCommand(CommandSender sender, String[] args) {
        String commandName = args[0];
        if (commandMap.containsKey(commandName)) {
            commandMap.get(commandName).accept(sender, args);
        } else {
            sender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Command not found!"));
        }
    }
}
