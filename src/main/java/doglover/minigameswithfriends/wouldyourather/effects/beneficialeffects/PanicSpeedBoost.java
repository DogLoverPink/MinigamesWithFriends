package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PanicSpeedBoost extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(PanicSpeedBoost.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Panic and get a speed boost on damage";
    }

    public PanicSpeedBoost(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(EntityDamageEvent.class);
        subscribeToEvent(EntityDamageByEntityEvent.class);
        subscribeToEvent(EntityDamageByBlockEvent.class);
    }

    private int panicTicks = -1;

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        giveSpeedBoost();
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        giveSpeedBoost();
    }

    @Override
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        giveSpeedBoost();
    }

    private void giveSpeedBoost() {
        panicTicks = 60;
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, panicTicks, 2, true, false, false));
    }

    @Override
    public void onTick() {
        if (panicTicks-- > 0) {
            getPlayer().getWorld().spawnParticle(Particle.WHITE_SMOKE, getPlayer().getLocation().add(0.0, 1.0, 0.0), 1, 0, 0, 0, 0, null);
        }
    }

    boolean highNote = false;

    @Override
    public void on4HertzTick() {
        if (panicTicks > 0) {
            highNote = !highNote;
            float pitch = highNote ? 1.8f : 1.5f;
            getPlayer().playSound(getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, pitch);
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
