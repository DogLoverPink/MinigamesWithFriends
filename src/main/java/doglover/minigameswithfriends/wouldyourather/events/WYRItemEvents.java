package doglover.minigameswithfriends.wouldyourather.events;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class WYRItemEvents implements Listener {

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerAttemptPickupItem(event);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerDropItem(event);
            }
        }
    }

    @EventHandler
    public void onPlayerStopUsingItem(PlayerStopUsingItemEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerStopUsingItem(event);
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!WYREventHandler.isActive()) {
            return;
        }
        for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
            effect.onPlayerItemConsume(event);
        }
    }
}
