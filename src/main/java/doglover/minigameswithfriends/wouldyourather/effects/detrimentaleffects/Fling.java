package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.PlayerUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

public class Fling extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(Fling.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You get flung!";
    }

    public Fling(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerToggleSneakEvent.class);
    }

    int counter = 0;
    Arrow arrow = null;

    @Override
    public void onTick() {
        if (counter++ >= 15) {
            if (counter == 16) {
                double xVec = 1.5 * Math.signum(getPlayer().getLocation().getDirection().getX());
                double zVec = 1.5 * Math.signum(getPlayer().getLocation().getDirection().getX());
                Vector vector = new Vector(xVec, 1.5, zVec);
                arrow = getPlayer().getWorld().spawnArrow(getPlayer().getLocation(), vector, 3, 12);
                arrow.addPassenger(getPlayer());
            }
            if (arrow.isInBlock() || arrow.isDead() || arrow.isInWater()) {
                this.selfDestruct();
            }
        }


    }

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.getPlayer().equals(getPlayer())) {
            event.setCancelled(true);
        }
    }

    NamespacedKey noFallKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "noFallDamage" + getUniqueNumber());

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        getPlayer().getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER).addModifier(new AttributeModifier(noFallKey, -5, AttributeModifier.Operation.ADD_NUMBER));
        PlayerUtils.launchPlayerToLoc(getPlayer(), getPlayer().getLocation().clone().add(0, 10, 0));
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (arrow != null) {
            arrow.remove();
        }
        getPlayer().getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER).removeModifier(noFallKey);
    }
}
