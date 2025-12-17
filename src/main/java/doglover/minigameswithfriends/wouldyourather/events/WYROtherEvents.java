package doglover.minigameswithfriends.wouldyourather.events;

import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class WYROtherEvents implements Listener {

    @EventHandler
    public void onCreeperIgnite(CreeperIgniteEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onCreeperIgnite(event);
            }
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onExplosionPrime(event);
            }
        }
    }
}
