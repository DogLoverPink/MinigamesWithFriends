package doglover.dimensionSwap.events;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.Game;
import doglover.dimensionSwap.gamemodes.DeathSwapGamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.damage.DamageType;
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
        if (!game.isInDeathMatch() && game.isGamemodeActive(DeathSwapGamemode.class)) {
            int pointsToGive = game.getConfig().getDeathSwapConfig().getPointsPerImpressiveDeath();
            if (event.getDamageSource().getDamageType() == DamageType.FALL
                    || event.getDamageSource().getDamageType() == DamageType.LAVA
                    || event.getDamageSource().getDamageType() == DamageType.OUT_OF_WORLD) {
                pointsToGive = game.getConfig().getDeathSwapConfig().getPointsPerLameDeath();
            }
            game.addPointsToPlayer(game.getGamemode(DeathSwapGamemode.class).getSwapperFromSwapee(event.getPlayer()), pointsToGive);
            game.getGamemode(DeathSwapGamemode.class).nullifySwapper(event.getPlayer());
        }
    }

}
