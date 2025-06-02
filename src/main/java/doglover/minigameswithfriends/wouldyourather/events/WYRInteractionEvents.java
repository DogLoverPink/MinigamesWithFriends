package doglover.minigameswithfriends.wouldyourather.events;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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
