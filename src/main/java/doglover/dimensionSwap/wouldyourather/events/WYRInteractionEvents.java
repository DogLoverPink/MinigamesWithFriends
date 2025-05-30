package doglover.dimensionSwap.wouldyourather.events;

import doglover.dimensionSwap.wouldyourather.WYREffect;
import doglover.dimensionSwap.wouldyourather.WYREventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class WYRInteractionEvents implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!WYREventHandler.isActive() || event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
            effect.onPlayerInteract(event);
        }
    }
}
