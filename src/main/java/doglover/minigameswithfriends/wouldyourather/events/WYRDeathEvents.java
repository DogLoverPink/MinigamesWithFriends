package doglover.minigameswithfriends.wouldyourather.events;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
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
