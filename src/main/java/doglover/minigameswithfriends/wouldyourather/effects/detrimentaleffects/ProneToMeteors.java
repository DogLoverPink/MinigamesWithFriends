package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProneToMeteors extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(ProneToMeteors.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You're suspiciously prone to meteor strikes";
    }

    public ProneToMeteors(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(ProjectileHitEvent.class);
    }

    List<ShulkerBullet> trackedEntities = new ArrayList<>();

    @Override
    public void onTick() {
        trackedEntities.removeIf(ShulkerBullet::isDead);
        spawnParticlesAroundMeteors();
    }

    @Override
    public void on4HertzTick() {
        boolean isAboveSeaLevel = getPlayer().getLocation().getBlockY() + 5 > getPlayer().getWorld().getSeaLevel();
        if (NumberUtils.chanceOf(0.0325) && isAboveSeaLevel) {
            spawnMeteor();
        }
        playSoundsAroundMeteors();
    }

    private void playSoundsAroundMeteors() {
        for (ShulkerBullet meteor : trackedEntities) {
            if (meteor.isDead()) {
                continue;
            }
            Location loc = meteor.getLocation();
            loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.5f);
        }
    }

    private void spawnParticlesAroundMeteors() {
        for (ShulkerBullet meteor : trackedEntities) {
            if (meteor.isDead()) {
                continue;
            }
            Location loc = meteor.getLocation();
            loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 10);
        }
    }

    private void spawnMeteor() {
        int randX = random.nextInt(-3, 3);
        int randZ = random.nextInt(-3, 3);
        Location spawnLoc = getPlayer().getLocation().clone().add(randX, random.nextInt(15, 40), randZ);
        ShulkerBullet meteor = getPlayer().getWorld().spawn(spawnLoc, ShulkerBullet.class);
        trackedEntities.add(meteor);
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof ShulkerBullet meteor)) {
            return;
        }
        if (!trackedEntities.contains(meteor)) {
            return;
        }
        trackedEntities.remove(meteor);
        meteor.getWorld().createExplosion(meteor.getLocation(), 2.5f);
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
