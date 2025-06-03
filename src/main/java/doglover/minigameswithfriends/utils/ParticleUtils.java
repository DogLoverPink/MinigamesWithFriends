package doglover.minigameswithfriends.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ParticleUtils {
    public static void summonCircle(Location location, double radius, Particle particle, Block block) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * radius);
            particleLoc.setZ(location.getZ() + Math.sin(d) * radius);
            particleLoc.setY(location.getY() - 0.05);
            location.getWorld().spawnParticle(particle, particleLoc, 1, block.getBlockData());

        }
    }

    public static void summonCircle(Player plr, Location location, double radius, Particle particle,
                                    Particle.DustOptions dustOptions) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * radius);
            particleLoc.setZ(location.getZ() + Math.sin(d) * radius);
            particleLoc.setY(location.getY() - 0.05);
            plr.spawnParticle(particle, particleLoc, 1, dustOptions);

        }
    }

    public static void summonCircle(Location location, double radius, Particle particle,
                                    Particle.DustOptions dustOptions) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * radius);
            particleLoc.setZ(location.getZ() + Math.sin(d) * radius);
            particleLoc.setY(location.getY() - 0.05);
            location.getWorld().spawnParticle(particle, particleLoc, 1, dustOptions);

        }
    }
}
