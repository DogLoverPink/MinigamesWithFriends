package doglover.dimensionSwap.commands;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.configs.GamemodeConfig;
import doglover.dimensionSwap.gamemodes.Gamemode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameCommandTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("AddPlayer", "start", "stop", "config", "EnableGamemode", "DisableGamemode", "ClearGamemodes", "fling");
        }
        if (args[0].equalsIgnoreCase("config")) {
            return handleConfigTagComplete(args);
        } else if (args[0].equalsIgnoreCase("EnableGamemode") || args[0].equalsIgnoreCase("DisableGamemode")) {
            return Gamemode.getGamemodeList();
        }

        return null;
    }

    private static @Nullable List<String> handleConfigTagComplete(String[] args) {
        if (args.length == 2) {
            List<String> gamemodes = new ArrayList<>(Gamemode.getGamemodeList());
            gamemodes.add("mainGame");
            return gamemodes;
        }
        if (args.length == 3) {
            String configName = args[1];
            GamemodeConfig conf = DimensionSwap.getGame().getConfig().getGamemodeConfigFromName(configName);
            if (conf == null) {
                return List.of();
            }
            return conf.getConfigValues().keySet().stream().toList();
        }
        if (args.length == 4) {
            String configName = args[1];
            String key = args[2];
            GamemodeConfig conf = DimensionSwap.getGame().getConfig().getGamemodeConfigFromName(configName);
            if (conf == null) {
                return List.of();
            }
            Class<?> type = conf.getConfigValues().get(key);
            if (type == null) {
                return List.of();
            }
            if (type.equals(Boolean.class) ) {
                return List.of("true", "false");
            } else {
                return List.of();
            }
        }
        return null;
    }
}
