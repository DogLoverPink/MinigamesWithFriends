package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.checkerframework.checker.units.qual.A;

public class HigherStepHeight extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(HigherStepHeight.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Increase your vertical step height while not sneaking";
    }

    public HigherStepHeight(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerToggleSneakEvent.class);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "higher_step_height"+getUniqueNumber());

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player plr = event.getPlayer();
        if (!plr.equals(this.getPlayer())) {
            return;
        }
        if (event.isSneaking()) {
            plr.getAttribute(org.bukkit.attribute.Attribute.STEP_HEIGHT).removeModifier(key);
        } else {
            addModifier();
        }
    }

    private void addModifier() {
        AttributeModifier mod = new AttributeModifier(key, 1.6, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.STEP_HEIGHT).addModifier(mod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        if (!getPlayer().isSneaking()) {
            addModifier();
        }
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.STEP_HEIGHT).removeModifier(key);
    }
}
