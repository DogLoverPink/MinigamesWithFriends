package doglover.minigameswithfriends.utils;

import doglover.minigameswithfriends.MinigamesWithFriends;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityUtils {
    public static LivingEntity spawnEntityWithAnimation(Location location, Class<? extends LivingEntity> entityType) {

        LivingEntity entity = location.getWorld().spawn(location.clone().subtract(0, 2, 0), entityType);
        new BukkitRunnable() {
            int timeLeft = 20;
            Location loc = location.clone().subtract(0, 2, 0);

            @Override
            public void run() {
                timeLeft--;
                loc = loc.add(0, 0.1, 0);
                entity.teleport(loc);
                if (timeLeft == 16) {
                    entity.setInvulnerable(false);
                }
                ParticleUtils.summonCircle(location.clone().subtract(0, 0.25f, 0), 0.7f, Particle.BLOCK,
                        location.clone().subtract(0, 1, 0).getBlock());

                if (!MinigamesWithFriends.getGame().isRunning()) {
                    this.cancel();
                    entity.remove();
                    return;
                }
                if (timeLeft < 1) {
                    this.cancel();
                    entity.setInvulnerable(false);
                }

            }
        }.runTaskTimer(MinigamesWithFriends.getGamePlugin(), 0, 2);
        return entity;
    }
}
