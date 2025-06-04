package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class ReallyStrongGravity extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(ReallyStrongGravity.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You're far more affected by gravity";
    }

    public ReallyStrongGravity(Player player) {
        super(player);
        setRepeatable(false);
    }

    NamespacedKey gravKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "moreGravity"+getUniqueNumber());
    NamespacedKey jumpKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "moreGravityCompensation"+getUniqueNumber());


    private void addModifier() {
        AttributeModifier gravMod = new AttributeModifier(gravKey, 1, AttributeModifier.Operation.ADD_NUMBER);
        AttributeModifier jumpMod = new AttributeModifier(jumpKey, 1, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.GRAVITY).addModifier(gravMod);
        getPlayer().getAttribute(Attribute.JUMP_STRENGTH).addModifier(jumpMod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        addModifier();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.GRAVITY).removeModifier(gravKey);
        getPlayer().getAttribute(Attribute.JUMP_STRENGTH).removeModifier(jumpKey);
    }
}
