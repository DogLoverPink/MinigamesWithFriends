package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class ConcreteShoes extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(ConcreteShoes.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You bought your shoes from the 1940's mafia";
    }

    public ConcreteShoes(Player player) {
        super(player);
        setRepeatable(false);
    }

    @Override
    public void on4HertzTick() {
        if (getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        Block blockBelow = getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        Block blockAbove = getPlayer().getLocation().getBlock().getRelative(BlockFace.UP);
        if (getPlayer().isInWater() && (blockBelow.getType() == Material.WATER
                || blockAbove.getType() == Material.WATER)) {
            getPlayer().setVelocity(new Vector(0, -0.25, 0));
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
