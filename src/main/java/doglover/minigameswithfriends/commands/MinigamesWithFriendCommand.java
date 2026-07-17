package doglover.minigameswithfriends.commands;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.gamemodes.DimensionSwapGamemode;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.PlayerUtils;
import doglover.minigameswithfriends.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MinigamesWithFriendCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            commandSender.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Please specify a subcommand."));
            return true;
        }
        CommandHandler.handleCommand(commandSender, args);
        return true;
    }
}
