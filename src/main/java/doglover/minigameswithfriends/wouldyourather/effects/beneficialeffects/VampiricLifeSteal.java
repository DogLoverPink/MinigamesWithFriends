package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class VampiricLifeSteal extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(VampiricLifeSteal.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get vampiric life steal upon dealing damage";
    }

    public VampiricLifeSteal(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(EntityDamageByEntityEvent.class);
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity target)) {
            return;
        }
        if (!event.getDamager().equals(getPlayer())) {
            return;
        }
        double damage = event.getFinalDamage();
        double healthToRestore = Math.min(damage, target.getHealth()) * 0.25;
        player.heal(healthToRestore);
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
