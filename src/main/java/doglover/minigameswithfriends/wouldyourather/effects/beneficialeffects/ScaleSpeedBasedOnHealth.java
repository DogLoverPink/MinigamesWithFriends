package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class ScaleSpeedBasedOnHealth extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(ScaleSpeedBasedOnHealth.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Move faster or slower based on your current health";
    }

    AttributeModifier mod;

    @Override
    public void onTick() {
        Player player = getPlayer();
        if (player == null || !player.isOnline()) {
            return;
        }
        double speedToAdd = getHealthPercentage() * 0.1 - 0.06;
        mod = new AttributeModifier(new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "scale_speed_based_on_health"),
                speedToAdd, AttributeModifier.Operation.ADD_NUMBER);
        player.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(mod);
        player.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(mod);
    }

    private double getHealthPercentage() {
        Player player = getPlayer();
        if (player == null) {
            return 0.0;
        }
        return player.getHealth() / player.getAttribute(Attribute.MAX_HEALTH).getValue();
    }

    public ScaleSpeedBasedOnHealth(Player player) {
        super(player);
        setRepeatable(false);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(mod);
    }
}
