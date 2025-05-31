package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class RandomTeleport extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(RandomTeleport.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Your position in spacetime becomes unstable";
    }

    public RandomTeleport(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    @Override
    public void on4HertzTick() {
        if (NumberUtils.chanceOf(0.01)) {
            double x = getPlayer().getLocation().getX() + (Math.random() * 20 - 10);
            double y = getPlayer().getLocation().getY();
            double z = getPlayer().getLocation().getZ() + (Math.random() * 20 - 10);
            Location safeLoc = BlockUtils.findSafeBlock(new Location(getPlayer().getWorld(), x, y, z));
            if (player.getLocation().distanceSquared(safeLoc) < 100) {
                player.teleport(safeLoc);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
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
    }
}
