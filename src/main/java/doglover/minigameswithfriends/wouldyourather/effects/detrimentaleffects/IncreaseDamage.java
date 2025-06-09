package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.boat.OakBoat;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class IncreaseDamage extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(IncreaseDamage.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "All damage you take is at least 2 hearts";
    }

    public IncreaseDamage(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityDamageEvent.class);
        subscribeToEvent(EntityDamageByBlockEvent.class);
        subscribeToEvent(EntityDamageByEntityEvent.class);
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        increaseDamage(event);
    }
    @Override
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        increaseDamage(event);
    }
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        increaseDamage(event);
    }

    private void increaseDamage(EntityDamageEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        if (event.getDamage() < 4) {
            event.setDamage(4);
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
