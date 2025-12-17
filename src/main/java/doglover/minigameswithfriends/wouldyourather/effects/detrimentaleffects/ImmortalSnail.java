package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.ParticleUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ImmortalSnail extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(ImmortalSnail.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You get chased by an immortal snail";
    }

    private Silverfish snail;

    public ImmortalSnail(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityDamageByEntityEvent.class);
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getDamager().equals(snail)) {
            return;
        }
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        getPlayer().removePotionEffect(PotionEffectType.RESISTANCE);
        getPlayer().getWorld().strikeLightningEffect(getPlayer().getLocation());
        ParticleUtils.createParticleCloud(getPlayer().getLocation(), 5, Particle.SMOKE, 180);
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
        getPlayer().damage(100000);
    }

    Random random = new Random();

    @Override
    public void on4HertzTick() {
        if (!snail.isValid()) {
            spawnSnail();
        }
        if (!snail.getWorld().equals(getPlayer().getWorld())) {
            snail.teleport(getAppropiateSnailLocation());
        }
        if (snail.getTarget() != getPlayer()) {
            snail.setTarget(getPlayer());
        }
        if (snail.getLocation().distanceSquared(getPlayer().getLocation()) > 324) {
            snail.teleport(getAppropiateSnailLocation());
        }
    }

    private Location getAppropiateSnailLocation() {
        Location plrLoc = getPlayer().getLocation();
        double xLocAdd = (random.nextBoolean() ? -12.0 : 12.0);
        double zLocAdd = (random.nextBoolean() ? -12.0 : 12.0);
        return BlockUtils.findSafeLocationInRadius(plrLoc.clone().add(xLocAdd, 0, zLocAdd), 10);
    }

    private void spawnSnail() {
        Location snailSpawnLoc = getAppropiateSnailLocation();
        snail = getPlayer().getWorld().spawn(snailSpawnLoc, Silverfish.class);
        snail.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 9, true));
        snail.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 9, true));
        snail.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.13);
        snail.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(25);
        snail.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(10);
        snail.setPersistent(true);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        spawnSnail();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (snail.isValid()) {
            snail.remove();
        }
    }
}
