package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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
                Block block = loc.getBlock();
                BlockUtils.convertBlockToFallingBlock(block, true);
            }
        }

    }

    private List<Location> getNonAirLocations(Location loc) {
        List<Location> locations = new ArrayList<>();
        double totalHardness = 0.0;
        for (int i = 1; totalHardness <= 4; i++) {
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
        for (int i = 1; totalHardness <= 4; i++) {
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
