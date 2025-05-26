package doglover.dimensionSwap.events;

import doglover.dimensionSwap.DimensionSwap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DimensionSwap.getGame().reportPlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        DimensionSwap.getGame().reportPlayerQuit(event.getPlayer());
    }
}
