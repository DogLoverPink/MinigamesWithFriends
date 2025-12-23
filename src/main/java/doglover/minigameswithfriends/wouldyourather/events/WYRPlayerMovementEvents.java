package doglover.minigameswithfriends.wouldyourather.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class WYRPlayerMovementEvents implements Listener {

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerToggleSneak(event);
            }
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerJump(event);
            }
        }
    }

    @EventHandler
    public void onPlayerInput(PlayerInputEvent event) {
        if (WYREventHandler.isActive()) {
            for (WYREffect effect : WYREventHandler.getEffectsForEvent(event)) {
                effect.onPlayerInput(event);
            }
        }
    }
}
