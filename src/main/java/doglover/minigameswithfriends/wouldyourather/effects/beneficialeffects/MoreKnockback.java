package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class MoreKnockback extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(MoreKnockback.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Deal significantly more knockback";
    }

    public MoreKnockback(Player player) {
        super(player);
        setRepeatable(true);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "more_knockback"+getUniqueNumber());


    private void addModifier() {
        AttributeModifier mod = new AttributeModifier(key, 1.6, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.ATTACK_KNOCKBACK).addModifier(mod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        addModifier();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.ATTACK_KNOCKBACK).removeModifier(key);
    }
}
