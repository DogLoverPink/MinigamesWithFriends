package doglover.minigameswithfriends.wouldyourather.effects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ExampleAttributeBased extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(ExampleAttributeBased.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "";
    }

    public ExampleAttributeBased(Player player) {
        super(player);
        setRepeatable(true);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "effect_key"+getUniqueNumber());


    private void addModifier() {
        AttributeModifier mod = new AttributeModifier(key, 1.6, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.STEP_HEIGHT).addModifier(mod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        addModifier();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.STEP_HEIGHT).removeModifier(key);
    }
}
