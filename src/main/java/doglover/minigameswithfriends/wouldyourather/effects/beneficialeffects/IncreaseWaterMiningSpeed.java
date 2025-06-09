package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class IncreaseWaterMiningSpeed extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(IncreaseWaterMiningSpeed.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Mine twice as fast while submerged in water";
    }

    public IncreaseWaterMiningSpeed(Player player) {
        super(player);
        setRepeatable(true);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "water_mines_quicker"+getUniqueNumber());


    private void addModifier() {
        removeModifier();
        AttributeModifier mod = new AttributeModifier(key, 1.8, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.SUBMERGED_MINING_SPEED).addModifier(mod);
    }

    private void removeModifier() {
        getPlayer().getAttribute(Attribute.SUBMERGED_MINING_SPEED).removeModifier(key);
    }

    @Override
    public void on4HertzTick() {
        if (getPlayer().isUnderWater()) {
            addModifier();
        } else {
            removeModifier();
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        removeModifier();
    }
}
