package doglover.dimensionSwap.wouldyourather.events;

import doglover.dimensionSwap.wouldyourather.WYREffect;
import doglover.dimensionSwap.wouldyourather.WYREventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class WYRPlayerSneakEvent implements Listener {

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerToggleSneak(event);
            }
        }
    }
}
