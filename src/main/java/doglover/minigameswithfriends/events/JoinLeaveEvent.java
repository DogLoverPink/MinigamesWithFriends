package doglover.minigameswithfriends.events;

import doglover.minigameswithfriends.MinigamesWithFriends;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MinigamesWithFriends.getGame().reportPlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        MinigamesWithFriends.getGame().reportPlayerQuit(event.getPlayer());
    }
}
