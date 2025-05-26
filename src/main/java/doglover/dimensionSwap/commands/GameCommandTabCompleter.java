package doglover.dimensionSwap.commands;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.configs.GamemodeConfig;
import doglover.dimensionSwap.gamemodes.BlockShuffleGamemode;
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
            return filterByStartsWith(List.of("start", "stop", "config", "EnableGamemode", "DisableGamemode", "ClearGamemodes", "blockshuffle"), args[0]);
        }
        String minigameCommand = args[0];
        if (minigameCommand.equalsIgnoreCase("config")) {
            return handleConfigTagComplete(args);
        } else if (minigameCommand.equalsIgnoreCase("EnableGamemode")) {
            return filterByStartsWith(Gamemode.getGamemodeList(), args[1]);
        } else if (minigameCommand.equalsIgnoreCase("DisableGamemode")) {
            return filterByStartsWith(DimensionSwap.getGame().getGamemodesAsString(), args[1]);
        }
        if (minigameCommand.equalsIgnoreCase("blockshuffle")) {
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

        return null;
    }

    private List<String> filterByStartsWith(List<String> list, String filter) {
        return list.stream().filter(s -> s.toLowerCase().startsWith(filter.toLowerCase())).toList();
    }



    private @Nullable List<String> handleConfigTagComplete(String[] args) {
        if (args.length == 2) {
            List<String> gamemodes = new ArrayList<>(Gamemode.getGamemodeList());
            gamemodes.add("mainGame");
            return filterByStartsWith(gamemodes, args[1]);
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
                return filterByStartsWith(List.of("true", "false"), args[3]);
            } else {
                return List.of();
            }
        }
        return null;
    }
}
