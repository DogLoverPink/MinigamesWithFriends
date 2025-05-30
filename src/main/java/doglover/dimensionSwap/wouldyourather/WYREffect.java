package doglover.dimensionSwap.wouldyourather;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public abstract class WYREffect {

    private boolean isBeneficial;

    private final Player player;

    public void setIsBeneficial(boolean beneficial) {
        isBeneficial = beneficial;
    }

    public boolean isBeneficial() {
        return isBeneficial;
    }

    public Player getPlayer() {
        return player;
    }

    public WYREffect(Player player) {
        this.player = player;
    }

    protected void subscribeToEvent(Class<? extends Event> eventClass) {
        WYREventHandler.subscribe(eventClass, this);
    }

    public abstract String getDescriptionBlurb();

    public void onEffectInitiate() {

    }

    public void onEffectDecompose() {
        WYREventHandler.unsubscribe(this);
    }


    public void onPlayerDeath(PlayerDeathEvent event) {

    }

    public void onEntityDeath(EntityDeathEvent event) {

    }

    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {

    }

}
