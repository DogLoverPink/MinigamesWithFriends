package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class My600PoundLife extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(My600PoundLife.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You weigh 850 pounds";
    }

    public My600PoundLife(Player player) {
        super(player);
        setRepeatable(false);
    }

    @Override
    public void onTick() {
        if (getPlayer() == null || !getPlayer().isOnline()) {
            return;
        }

        Location playerLocation = getPlayer().getLocation();
        boolean isSolidGround = playerLocation.clone().subtract(0, 0.5, 0).getBlock().getType().isSolid();
        if (isSolidGround && hasAirBelow(playerLocation)) {
            List<Location> nonAirLocations = getNonAirLocations(playerLocation);
            if (nonAirLocations.isEmpty()) {
                return;
            }
            World world = playerLocation.getWorld();
            world.playSound(playerLocation, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.0f, 1.0f);
            for (Location loc : nonAirLocations) {
                BlockData data = loc.getBlock().getBlockData();
                loc.setX(Math.floor(loc.getX()) + 0.5);
                loc.setZ(Math.floor(loc.getZ()) + 0.5);
                world.spawn(loc, FallingBlock.class,
                        fallingBlock -> fallingBlock.setBlockData(data)
                );
                loc.getBlock().setType(Material.AIR);
            }
        }

    }

    private List<Location> getNonAirLocations(Location loc) {
        List<Location> locations = new ArrayList<>();
        double totalHardness = 0.0;
        for (int i = 1; totalHardness <= 2; i++) {
            Block below = loc.clone().subtract(0, i, 0).getBlock();
            if (below.getType().getHardness() < 0.0f || below.getType().isAir()) {
                break;
            }
            locations.add(below.getLocation());
            totalHardness += below.getType().getHardness();
        }
        return locations;
    }

    private boolean hasAirBelow(Location loc) {
        double totalHardness = 0.0;
        for (int i = 1; totalHardness <= 2; i++) {
            Block block = loc.clone().subtract(0, i, 0).getBlock();
            totalHardness += block.getType().getHardness();
            if (block.getType().getHardness() < 0.0f) {
                break;
            }
            if (block.getType().isAir()) {
                return true;
            }
        }
        return false;

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
