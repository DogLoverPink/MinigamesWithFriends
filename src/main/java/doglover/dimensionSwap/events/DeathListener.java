package doglover.dimensionSwap.events;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!DimensionSwap.getGame().isRunning()) {
            return;
        }

        Game game = DimensionSwap.getGame();
        if (game.isInDeathMatch()) {
            event.setCancelled(true);
            game.reportDeathmatchDeath(event.getPlayer());
        } else {
            Player player = event.getPlayer();
            Location respawnLocation = player.getWorld().getSpawnLocation();
            if (player.getRespawnLocation() != null) {
                if (player.getRespawnLocation().getWorld().equals(respawnLocation.getWorld())) {
                    respawnLocation = player.getRespawnLocation();
                }
            }
            Location finalRespawnLocation = respawnLocation;
            Bukkit.getScheduler().runTaskLater(DimensionSwap.getGamePlugin(), () -> {
                event.getEntity().spigot().respawn();
                if (!game.isInDeathMatch()) {
                    event.getEntity().teleport(finalRespawnLocation);
                }
            }, 1L);
        }
    }

}
