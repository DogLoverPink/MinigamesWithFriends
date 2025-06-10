package doglover.minigameswithfriends.wouldyourather.events;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class WYRBlockEvents implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onBlockBreak(event);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onBlockPlace(event);
            }
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onBlockDamage(event);
            }
        }
    }

    @EventHandler
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onBlockDamageAbort(event);
            }
        }
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onBlockDropItem(event);
            }
        }
    }
}
