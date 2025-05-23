package doglover.dimensionSwap.events;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.Game;
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
        }
    }

}
