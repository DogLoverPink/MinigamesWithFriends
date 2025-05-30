package doglover.dimensionSwap.wouldyourather.events;

import doglover.dimensionSwap.wouldyourather.WYREffect;
import doglover.dimensionSwap.wouldyourather.WYREventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class WYRDeathEvents implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerDeath(event);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onEntityDeath(event);
            }
        }
    }

}
