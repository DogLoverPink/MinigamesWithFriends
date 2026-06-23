package doglover.minigameswithfriends.events;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandBlockRunEvent implements Listener {

    @EventHandler
    public void onCommandBlockRun(ServerCommandEvent event) {
        if (event.getSender() instanceof ConsoleCommandSender) {
            return;
        }
        BlockCommandSender blockCommandSender = (BlockCommandSender) event.getSender();
        if (blockCommandSender.getBlock().getWorld().getName().contains("activedimensionswapworlds")) {
            event.setCancelled(true);
        }
    }
}
