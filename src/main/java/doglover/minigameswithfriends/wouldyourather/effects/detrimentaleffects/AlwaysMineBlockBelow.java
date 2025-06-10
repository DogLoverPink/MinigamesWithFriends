package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlwaysMineBlockBelow extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(AlwaysMineBlockBelow.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You are always mining the block below you";
    }

    public AlwaysMineBlockBelow(Player player) {
        super(player);
        setRepeatable(false);
    }

    private Block block;
    private float currentProgress;
    private float maxProgress;

    @Override
    public void onTick() {
        increaseBreakProgress(getNonAirBlockBelow());
    }


    private void increaseBreakProgress(Block newBlock) {
        if (newBlock == null) {
            return;
        }
        if (block == null || !block.equals(newBlock) || block.isEmpty()) {
            float time = (3.0f / newBlock.getDestroySpeed(getPlayer().getInventory().getItemInMainHand(), true)) * 20f;
            currentProgress = time;
            maxProgress = time;
            block = newBlock;
        }
        currentProgress--;
        float percentBroken = 1.0f - (currentProgress / maxProgress);
        if (percentBroken >= 1.0f) {
            newBlock.breakNaturally(getPlayer().getInventory().getItemInMainHand(), true);
            block = null;
            return;
        }
        getPlayer().sendBlockDamage(newBlock.getLocation(), percentBroken);

    }


    private Block getNonAirBlockBelow() {
        Location loc = getPlayer().getLocation();
        for (int i = 0; true; i++) {
            Block below = loc.clone().subtract(0, i + 0.4, 0).getBlock();
            if (i > 320) {
                return null;
            }
            if (below.isLiquid() || below.getType().isAir()) {
                continue;
            }
            if (below.getType().getHardness() < 0) {
                return null;
            }
            return below;
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
