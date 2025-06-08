package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TurnNearbyLeavesIntoCreepers extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(TurnNearbyLeavesIntoCreepers.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You turn nearby leaves into creepers (thanks EightSidedSquare) ";
    }

    public TurnNearbyLeavesIntoCreepers(Player player) {
        super(player);
        setRepeatable(false);
    }

    private int leafCounter = 0;

    Set<UUID> creepies = new HashSet<>();

    @Override
    public void on4HertzTick() {
        List<Location> locs = BlockUtils.getNearbyNonAirBlocks(getPlayer().getLocation(), 11);
        for (Location loc : locs) {
            Block block = getPlayer().getWorld().getBlockAt(loc);
            if (!Tag.LEAVES.isTagged(block.getType())) {
                continue;
            }
            leafCounter++;
            block.setType(Material.AIR);
            if (leafCounter >= 18) {
                creepies.add(getPlayer().getWorld().spawnEntity(loc.clone().subtract(0, 1, 0), EntityType.CREEPER).getUniqueId());
                leafCounter = 0;
            }
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        for (UUID uuid : creepies) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null && !entity.isDead()) {
                entity.remove();
            }
        }
    }
}
