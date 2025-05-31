package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.utils.PlayerUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class MagneticBody extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(MagneticBody.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You accidentally swallowed 140 magnetic balls";
    }

    public MagneticBody(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    static final List<Material> metal = List.of(
            Material.IRON_BLOCK,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.ANVIL,
            Material.CHIPPED_ANVIL,
            Material.DAMAGED_ANVIL,
            Material.IRON_BARS,
            Material.IRON_DOOR,
            Material.IRON_TRAPDOOR,
            Material.CHAIN,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.CAULDRON,
            Material.SPAWNER,
            Material.TRIAL_SPAWNER,
            Material.VAULT,
            Material.HOPPER
    );

    @Override
    public void on4HertzTick() {
        if (NumberUtils.chanceOf(0.06)) {
            Location metalLoc = BlockUtils.findLocationOfBlockType(metal, player.getLocation(), 10);
            if (metalLoc == null) {
                return;
            }
            PlayerUtils.launchPlayerToLoc(player, metalLoc);
        }
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
