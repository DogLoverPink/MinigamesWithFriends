package doglover.minigameswithfriends.events;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.configs.DeathSwapConfig;
import doglover.minigameswithfriends.gamemodes.DeathSwapGamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!MinigamesWithFriends.getGame().isRunning()) {
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
        if (!game.isInDeathMatch() && game.isGamemodeActive(DeathSwapGamemode.class)) {
            DeathSwapGamemode deathSwapGamemode = game.getGamemode(DeathSwapGamemode.class);
            DeathSwapConfig config = game.getConfig().getDeathSwapConfig();

            Player swapper = deathSwapGamemode.getSwapperFromSwapee(event.getPlayer());

            if (config.shouldKeepInventoryOnSwapRelatedDeath() && swapper != null) {
                event.setKeepInventory(true);
            }
            if (swapper == null) {
                return;
            }
            int pointsToGive = config.getPointsPerImpressiveDeath();
            if (event.getDamageSource().getDamageType() == DamageType.FALL
                    || event.getDamageSource().getDamageType() == DamageType.LAVA
                    || event.getDamageSource().getDamageType() == DamageType.OUT_OF_WORLD) {
                pointsToGive = config.getPointsPerLameDeath();
            }
            game.addPointsToPlayer(swapper, pointsToGive);
            deathSwapGamemode.nullifySwapper(event.getPlayer());
        }
    }

}
