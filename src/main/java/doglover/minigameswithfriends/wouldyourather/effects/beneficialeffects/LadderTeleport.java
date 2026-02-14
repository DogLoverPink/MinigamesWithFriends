package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.utils.ParticleUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LadderTeleport extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(LadderTeleport.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to instantly climb ladders";
    }

    public LadderTeleport(Player player) {
        super(player);
        setRepeatable(false);
    }

    Location lastPosition;
    int debounce = 3;

    @Override
    public void onTick() {
        Location currentLoc = getPlayer().getLocation();
        if (lastPosition == null || !lastPosition.getWorld().equals(getPlayer().getWorld())) {
            lastPosition = currentLoc;
        }
        if (debounce > 0) {
            debounce--;
            lastPosition = currentLoc;
            return;
        }
        if (!getPlayer().isClimbing()) {
            lastPosition = currentLoc;
        } else {
            boolean goingUp = currentLoc.getY() >= lastPosition.getY();
            Location toLoc = getEndOfLadder(getPlayer().getLocation(), goingUp);
            toLoc.setDirection(getPlayer().getLocation().getDirection());
            ParticleUtils.drawLine(getPlayer().getLocation(), toLoc, Particle.DUST, new Particle.DustOptions(Color.PURPLE, 1));
            getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            getPlayer().teleport(toLoc);
            debounce = 20;
        }
    }

    private Location getEndOfLadder(Location playerLoc, boolean goingUp) {
        Block block = playerLoc.getBlock();
        Material type = block.getType();
        Location loc = block.getLocation();
        int increment = goingUp ? 1 : -1;
        int i = increment;
        while (getPlayer().getWorld().getBlockAt(loc.clone().add(0, i, 0)).getType() == type) {
            i += increment;
        }
        if (!goingUp) {
            i++;
        }
        return playerLoc.clone().add(0, i, 0);

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
