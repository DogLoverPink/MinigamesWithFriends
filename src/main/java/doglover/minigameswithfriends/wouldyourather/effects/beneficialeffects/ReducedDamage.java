package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ReducedDamage extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(ReducedDamage.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Have all damage taken reduced by 1";
    }

    public ReducedDamage(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(EntityDamageEvent.class);
        subscribeToEvent(EntityDamageByBlockEvent.class);
        subscribeToEvent(EntityDamageByEntityEvent.class);
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        reduceDamage(event);
    }
    @Override
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        reduceDamage(event);
    }
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        reduceDamage(event);
    }

    private void reduceDamage(EntityDamageEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        double newDamage = event.getDamage() - 1;
        if (newDamage < 0.02) {
            event.setCancelled(true);
        }
        event.setDamage(newDamage);
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
