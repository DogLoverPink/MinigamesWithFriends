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
        WYREffectHandler.registerDetrimentalWYREffect(ReallyStrongGravity.class);
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

    boolean modsRemoved = false;

    private void addModifier() {
        AttributeModifier gravMod = new AttributeModifier(gravKey, 1, AttributeModifier.Operation.ADD_NUMBER);
        AttributeModifier jumpMod = new AttributeModifier(jumpKey, 1, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.GRAVITY).addModifier(gravMod);
        getPlayer().getAttribute(Attribute.JUMP_STRENGTH).addModifier(jumpMod);
    }

    private void removeModifier() {
        getPlayer().getAttribute(Attribute.GRAVITY).removeModifier(gravKey);
        getPlayer().getAttribute(Attribute.JUMP_STRENGTH).removeModifier(jumpKey);
    }

    @Override
    public void on4HertzTick() {
        if (!modsRemoved && getPlayer().isInWater()) {
            removeModifier();
            modsRemoved = true;
        }
        if (modsRemoved && !getPlayer().isInWater()) {
            addModifier();
            modsRemoved = false;
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        addModifier();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        removeModifier();
    }
}
