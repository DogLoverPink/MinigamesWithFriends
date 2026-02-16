package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GroundSlam extends WYREffect {

    static {
        WYREffectHandler.registerBeneficialWYREffect(GroundSlam.class);
    }

    public GroundSlam(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerToggleSneakEvent.class);
        subscribeToEvent(EntityDamageEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.SAFE_FALL_DISTANCE).removeModifier(key);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        if (!getPlayer().isSneaking() || getPlayer().getFallDistance() <= 0.1) {
            return;
        }
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }
        event.setDamage(event.getDamage() / 4);
        //Lerp from 0-20, to 0-3
        double radius = Math.min(getPlayer().getFallDistance() / 20.0 * 3.0, 3.0) + 0.15;
        double knockbackStrength = Math.min(getPlayer().getFallDistance() / 20.0 * 1.5 + 0.5, 2.0);

        createGroundSlamParticles(getPlayer().getLocation(), radius, knockbackStrength);
        for (Entity damagee : event.getEntity().getNearbyEntities(radius, 2, radius)) {
            if (!(damagee instanceof LivingEntity fallenOnEntity)) {
                continue;
            }
            if (fallenOnEntity.equals(getPlayer())) {
                continue;
            }
            fallenOnEntity.damage(event.getDamage() * 4, getPlayer());

            Vector direction = fallenOnEntity.getLocation().toVector().subtract(getPlayer().getLocation().toVector()).normalize();
            Vector knockback = direction.multiply(0.8).setY(0.4);
            knockback.multiply(knockbackStrength);
            fallenOnEntity.setVelocity(fallenOnEntity.getVelocity().add(knockback));

        }
    }

    private void createGroundSlamParticles(Location location, double radius, double strength) {
        Location groundLocation = location.clone().add(0, 0.1, 0);

        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, (float) strength, 0.8f);

        location.getWorld().spawnParticle(Particle.EXPLOSION, groundLocation, (int) (5 * strength));
        location.getWorld().spawnParticle(Particle.CLOUD, groundLocation, (int) (20 * strength), 0.1, 0.1, 0.1, 0.1);

        for (int ring = 1; ring <= 3; ring++) {
            double ringRadius = radius * ring / 3.0;
            int particleCount = (int) (ringRadius * 20);

            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount;
                double x = ringRadius * Math.cos(angle);
                double z = ringRadius * Math.sin(angle);

                Location particleLoc = groundLocation.clone().add(x, 0, z);

                location.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLoc, 1, 0, 0, 0, 0);
                location.getWorld().spawnParticle(Particle.BLOCK, particleLoc, 3, 0.1, 0.1, 0.1, 0.1,
                        particleLoc.getBlock().getRelative(0, -1, 0).getBlockData());
            }
        }
        Material groundMaterial = groundLocation.getBlock().getRelative(0, -1, 0).getType();
        if (groundMaterial.isSolid() && !groundMaterial.isAir()) {
            location.getWorld().spawnParticle(Particle.ITEM, groundLocation, (int) (30 * strength),
                    radius * 0.5, 0.2, radius * 0.5, 0.2,
                    new ItemStack(groundMaterial));
        }


    }

    @Override
    public void onTick() {
        super.onTick();
        if (!getPlayer().isSneaking() || getPlayer().getFallDistance() <= 0.1) {
            return;
        }
        getPlayer().setVelocity(getPlayer().getVelocity().add(new Vector(0, -0.5, 0)));
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to ground slam by shifting in the air";
    }


    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "groundslam" + getUniqueNumber());

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player plr = event.getPlayer();
        if (!plr.equals(this.getPlayer())) {
            return;
        }
        if (!event.isSneaking()) {
            getPlayer().getAttribute(Attribute.SAFE_FALL_DISTANCE).removeModifier(key);
        }
        if (getPlayer().getFallDistance() <= 0.1) {
            return;
        }
        AttributeModifier mod = new AttributeModifier(key, -3, AttributeModifier.Operation.ADD_NUMBER);
        if (getPlayer().getAttribute(Attribute.SAFE_FALL_DISTANCE).getModifier(key) == null) {
            getPlayer().getAttribute(Attribute.SAFE_FALL_DISTANCE).addModifier(mod);
        }
        getPlayer().setVelocity(getPlayer().getVelocity().add(new Vector(0, -0.5, 0)));

    }
}
