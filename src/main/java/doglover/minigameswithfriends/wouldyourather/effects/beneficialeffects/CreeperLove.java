package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import doglover.minigameswithfriends.utils.ParticleUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class CreeperLove extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(CreeperLove.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Creepers love you!";
    }

    public CreeperLove(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(ExplosionPrimeEvent.class);
    }



    @Override
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (!(event.getEntity() instanceof Creeper creeper)) {
            return;
        }
        if (creeper.getTarget() == null || !creeper.getTarget().equals(getPlayer())) {
            return;
        }
        event.setCancelled(true);
        ParticleUtils.createParticleCloud(creeper.getLocation(), 5, Particle.HEART, 120);
        if (getPlayer().getWorld().equals(creeper.getWorld()) && getPlayer().getLocation().distanceSquared(creeper.getLocation()) < 25) {
            getPlayer().heal(4);
        }
        creeper.getWorld().playSound(creeper.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        creeper.remove();
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
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
