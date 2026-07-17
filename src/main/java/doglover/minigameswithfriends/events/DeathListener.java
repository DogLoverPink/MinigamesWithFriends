package doglover.minigameswithfriends.events;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!MinigamesWithFriends.getGame().isRunning() || MinigamesWithFriends.getGame().isPaused()) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (MinigamesWithFriends.getGame().getConfig().shouldKeepInventoryOnDeath()) {
            event.setKeepInventory(true);
        }

        Game game = MinigamesWithFriends.getGame();
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
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
                event.getEntity().spigot().respawn();
                if (!game.isInDeathMatch()) {
                    event.getEntity().teleport(finalRespawnLocation);
                }
            }, 1L);
        }
    }

}
