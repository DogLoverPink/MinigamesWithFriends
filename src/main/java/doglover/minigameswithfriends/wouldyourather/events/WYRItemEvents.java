package doglover.minigameswithfriends.wouldyourather.events;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class WYRItemEvents implements Listener {

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerAttemptPickupItem(event);
            }
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onEntityPickupItem(event);
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
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerItemDamage(event);
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
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onFoodLevelChange(event);
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
