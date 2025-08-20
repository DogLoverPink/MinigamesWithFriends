package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HeavyLeaves extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(HeavyLeaves.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Leaves around you become made of lead";
    }

    public HeavyLeaves(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityChangeBlockEvent.class);
    }


    Set<UUID> fallingBlocks = new HashSet<>();

    @Override
    public void on4HertzTick() {
        List<Block> leaves = new java.util.ArrayList<>();
        double radius = 2.5;
        Location playerLoc = getPlayer().getLocation();
        for (double x = -radius; x <= radius; x++) {
            for (double y = 0; y <= radius + 8; y++) {
                for (double z = -radius; z <= radius; z++) {
                    Location checkLoc = playerLoc.clone().add(x, y, z);
                    Block block = checkLoc.getBlock();
                    if (!block.getType().isAir()
                            && Tag.LEAVES.isTagged(block.getType())
                            && block.getLocation().clone().subtract(0, 1, 0).getBlock().getType().isAir()) {
                        leaves.add(block);
                    }
                }
            }
        }
        for (Block block : leaves) {

            Location loc = block.getLocation();
            BlockData data = block.getBlockData();
            loc.setX(Math.floor(loc.getX()) + 0.5);
            loc.setZ(Math.floor(loc.getZ()) + 0.5);
            FallingBlock fallingBlockEntity = getPlayer().getWorld().spawn(loc, FallingBlock.class,
                    fallingBlock -> fallingBlock.setBlockData(data)
            );
            fallingBlocks.add(fallingBlockEntity.getUniqueId());
            block.setType(Material.AIR);

        }
    }

    @Override
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!fallingBlocks.contains(event.getEntity().getUniqueId())) {
            return;
        }
        FallingBlock fallingBlock = (FallingBlock) event.getEntity();
        for (Entity entity : fallingBlock.getNearbyEntities(0.51, 0.51, 0.51)) {
            if (!entity.equals(getPlayer())) {
                continue;
            }
            getPlayer().damage(1.5);
            getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5f, 1.0f);
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        fallingBlocks.clear();
    }
}
