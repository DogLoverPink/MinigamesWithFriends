package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class ReflectProjectiles extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(ReflectProjectiles.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Reflect all shot projectiles";
    }

    public ReflectProjectiles(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(ProjectileHitEvent.class);
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!getPlayer().equals(event.getHitEntity())) {
            return;
        }
        Projectile projectile = event.getEntity();
        Vector newVelocity = projectile.getVelocity().multiply(-1).clone();
        Projectile newProjectile = (Projectile) getPlayer().getWorld().spawn(projectile.getLocation(), projectile.getType().getEntityClass());
        newProjectile.setVelocity(newVelocity);
        newProjectile.setShooter(getPlayer());
        projectile.remove();
        event.setCancelled(true);
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
