package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CliffDiving extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(CliffDiving.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You have bad luck when cliff diving";
    }

    public CliffDiving(Player player) {
        super(player);
        setRepeatable(false);
    }

    public ArrayList<Location> locsToClear = new ArrayList<>();

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onTick() {

        if (getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        Block below = getPlayer().getLocation().clone().subtract(0, 1, 0).getBlock();
        if (below.getType().isAir() || below.getType() == Material.POINTED_DRIPSTONE) {
            if (getPlayer().getLocation().subtract(0, 1.5, 0).getBlock().getType().isAir() || getPlayer().getLocation().subtract(0, 1.5, 0).getBlock().getType() == Material.POINTED_DRIPSTONE) {

            for (int i = 1; i < 360; i++) {
                Block block = getPlayer().getLocation().getBlock().getRelative(0, -i, 0);
                if (block.getType() == org.bukkit.Material.AIR) {
                    continue;
                }
                if (block.getType() == Material.POINTED_DRIPSTONE) {

                    break;
                }
                block.getRelative(0, 1, 0).setType(Material.POINTED_DRIPSTONE);
                locsToClear.add(block.getRelative(0, 1, 0).getLocation());
                break;
            }
        }
    } else {
            if (locsToClear != null) {
                for (Location loc : locsToClear) {
                    loc.getBlock().setType(Material.AIR);
                }
                locsToClear.clear();
            }
        }

    }


    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
