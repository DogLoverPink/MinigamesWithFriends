package doglover.minigameswithfriends.events;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerInventoryDropItemEvent implements Listener {

    @EventHandler
    public void onPlayerInventoryDropItem(PlayerDropItemEvent event) {
        Game game = MinigamesWithFriends.getGame();
        if (game.isRunning() && game.isGamemodeActive(WouldYouRatherGamemode.class)) {
            if (game.getGamemode(WouldYouRatherGamemode.class).isCurrentChoosing()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cTsk Tsk, I know what you're up to...");
            }
        }
    }
}
