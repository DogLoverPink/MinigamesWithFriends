package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WitherAway extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(WitherAway.class);
    }

    Map<Material, Material> replacementMap = new HashMap<Material, Material>();

    @Override
    public String getDescriptionBlurb() {
        return "You wither the terrain around you";
    }

    public WitherAway(Player player) {
        super(player);
        setRepeatable(false);
        populateReplacementMap();

    }

    public void populateReplacementMap() {

        Material[] mats = Material.values();
        for (Material mat : mats) {
            if (Tag.REPLACEABLE_BY_MUSHROOMS.isTagged(mat)) {
                replacementMap.put(mat, Material.WITHER_ROSE);
            }
            if (Tag.LEAVES.isTagged(mat)) {
                replacementMap.put(mat, Material.PALE_OAK_LEAVES);
            }
            if (Tag.LOGS.isTagged(mat)) {
                replacementMap.put(mat, Material.PALE_OAK_LOG);
            }



        }
        replacementMap.put(Material.GRASS_BLOCK, Material.MYCELIUM);
        replacementMap.put(Material.WILDFLOWERS, Material.WITHER_ROSE);
        replacementMap.remove(Material.WITHER_ROSE);
        replacementMap.remove(Material.WATER);
        replacementMap.remove(Material.LAVA);
    }


    public Set<Block> sphereAround(Location location, int radius) {
        Set<Block> sphere = new HashSet<Block>();
        Block center = location.getBlock();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block b = center.getRelative(x, y, z);
                    if (center.getLocation().distanceSquared(b.getLocation()) <= radius * radius) {
                        if (replacementMap.containsKey(b.getType())) {
                            b.setType(replacementMap.get(b.getType()));
                        }

                    }
                }
            }

        }
        return sphere;
    }

    @Override
    public void onTick() {
        sphereAround(getPlayer().getLocation(), 5);
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
