package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class RingOfFire extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(RingOfFire.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You fall into a burning ring of fire";
    }

    public RingOfFire(Player player) {
        super(player);
        setRepeatable(true);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        createLavaRing(getPlayer(), 3);
        this.selfDestruct();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }


    public void createLavaRing(Player player, double radius) {
        Location center = player.getLocation().getBlock().getLocation();
        center = center.add(0.5, 0, 0.5);
        World world = center.getWorld();

        int y = center.getBlockY() - 1;

        for (double angle = 0; angle < 360; angle += 10) {
            double rad = Math.toRadians(angle);
            double x = center.getX() + radius * Math.cos(rad);
            double z = center.getZ() + radius * Math.sin(rad);

            Block block = world.getBlockAt(new Location(world, x, y, z));

            Block above = block.getRelative(BlockFace.UP);
            if (above.getState() instanceof InventoryHolder || above.getType().getHardness() < 0) {
                continue;
            }
            above.setType(Material.LAVA);
        }
    }
}
