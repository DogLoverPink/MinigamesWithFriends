package doglover.minigameswithfriends.commands;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.configs.GamemodeConfig;
import doglover.minigameswithfriends.gamemodes.BlockShuffleGamemode;
import doglover.minigameswithfriends.gamemodes.Gamemode;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameCommandTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filterByStartsWith(List.of("help", "start", "stop", "config", "EnableGamemode", "DisableGamemode", "ClearGamemodes", "blockshuffle", "wouldyourather", "dimensionswap", "Pause", "Unpause", "AddSpectator", "RemoveSpectator"), args[0]);
        }
        String minigameCommand = args[0];
        if (minigameCommand.equalsIgnoreCase("config")) {
            return handleConfigTagComplete(args);
        } else if (minigameCommand.equalsIgnoreCase("wouldyourather")) {
                return handleWouldYouRatherTagComplete(args);
        } else if (minigameCommand.equalsIgnoreCase("EnableGamemode")) {
            return unEnabledGamemodeList(args[1]);
        } else if (minigameCommand.equalsIgnoreCase("DisableGamemode")) {
            return filterByStartsWith(MinigamesWithFriends.getGame().getGamemodesAsString(), args[1]);
        }
        else if (minigameCommand.equalsIgnoreCase("dimensionswap")) {
            return filterByStartsWith(List.of("PreLoadSavedDimensionSwapWorlds"), args[1]);
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

        } else if (minigameCommand.equalsIgnoreCase("addspectator")) {
            Set<Player> allPlayers = new HashSet<>(Bukkit.getOnlinePlayers());
            allPlayers.removeAll(MinigamesWithFriends.getGame().getSpectators());
            return allPlayers.stream().map(Player::getName).toList();
        } else if (minigameCommand.equalsIgnoreCase("removespectator")) {
            return MinigamesWithFriends.getGame().getSpectators().stream().map(Player::getName).toList();
        }

        return null;
    }

    private List<String> unEnabledGamemodeList(String input) {
        Game game = MinigamesWithFriends.getGame();
        List<String> gamemodes = Gamemode.getGamemodeClassList().stream()
                .filter(gamemode -> !game.isGamemodeActive(gamemode))
                .map(Gamemode::getGamemodeNameFromClass)
                .toList();
        return filterByStartsWith(gamemodes, input);
    }

    private List<String> handleWouldYouRatherTagComplete(String[] args) {
        if (args.length == 2) {
            return filterByStartsWith(List.of("RemoveEffect", "SendNewPrompt", "CheckActiveEffects"), args[1]);
        }
        if (args[1].equalsIgnoreCase("RemoveEffect")) {
            if (args.length == 3) {
                return filterByStartsWith(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[2]);
            }
            if (args.length == 4) {
                Player plr = Bukkit.getPlayer(args[2]);
                if (!WYREventHandler.isActive() || plr == null) {
                    return List.of();
                }
                return filterByStartsWith(WYREffectHandler.getManagedEffects().stream()
                        .filter(wyrEffect -> wyrEffect.getPlayer().equals(plr))
                        .map(wyrEffect -> wyrEffect.getClass().getSimpleName())
                        .toList(), args[3]);
            }
        }
        if (args[1].equalsIgnoreCase("CheckActiveEffects")) {
            return filterByStartsWith(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[2]);
        }
        return List.of();
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
            if (type.equals(Boolean.class) ) {
                return filterByStartsWith(List.of("true", "false"), args[3]);
            } else {
                return List.of();
            }
        }
        return null;
    }
}
