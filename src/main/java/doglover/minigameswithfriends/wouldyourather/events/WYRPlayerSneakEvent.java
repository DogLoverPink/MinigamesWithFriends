package doglover.minigameswithfriends.wouldyourather.events;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
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
