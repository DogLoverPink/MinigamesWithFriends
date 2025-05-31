package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class BlockRangeIncrease extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(BlockRangeIncrease.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Have your block range massively increased";
    }

    public BlockRangeIncrease(Player player) {
        super(player);
        setRepeatable(true);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "long_arms"+getUniqueNumber());


    private void addModifier() {
        AttributeModifier mod = new AttributeModifier(key, 5, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.BLOCK_INTERACTION_RANGE).addModifier(mod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        addModifier();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.BLOCK_INTERACTION_RANGE).removeModifier(key);
    }
}
