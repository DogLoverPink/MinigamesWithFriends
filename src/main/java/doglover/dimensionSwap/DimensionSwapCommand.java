package doglover.dimensionSwap;

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
            if (args.length == 3) {
                String key = args[1];
                String value = args[2];
                ((DimensionSwapGamemode) DimensionSwap.getGame().getGamemodes().get(0)).getConfig().setConfigValue(key, value);
                commandSender.sendMessage("§aConfig set: " + key + " = " + value);
            } else if (args.length == 2) {
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
            } else if (args.length == 1) {
                commandSender.sendMessage("§cPlease specify a key and a value.");
            }
        }

            return false;


    }
}
