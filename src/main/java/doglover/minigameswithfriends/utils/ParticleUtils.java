package doglover.minigameswithfriends.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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

    public static void createParticleCloud(Location loc, double radius, Particle particle, int count) {
        for (int i = 0; i < count; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double r = Math.random() * radius;
            double x = loc.getX() + r * Math.cos(angle);
            double z = loc.getZ() + r * Math.sin(angle);
            double y = loc.getY() + (Math.random() - 0.5) * radius;
            Location particleLoc = new Location(loc.getWorld(), x, y, z);
            loc.getWorld().spawnParticle(particle, particleLoc, 1);
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

    public static void drawLine(Location point1, Location point2, Particle particle) {
        drawLine(point1, point2, particle, null);
    }

    public static void drawLine(Location point1, Location point2, Particle particle, Particle.DustOptions dustOptions) {
        World world = point1.getWorld();
        if (world == null || !point2.getWorld().equals(world)) return;

        double distance = point1.distance(point2);
        Vector vector = point2.toVector().subtract(point1.toVector());
        vector.normalize();
        double space = 0.1;
        vector.multiply(space);

        Location currentLocation = point1.clone();
        double currentDistance = 0;

        for (; currentDistance < distance; currentLocation.add(vector)) {
            world.spawnParticle(particle, currentLocation, 1, dustOptions);
            currentDistance += space;
        }
    }
}
