package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class StubbyArms extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(StubbyArms.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Your block interaction range is reduced";
    }

    public StubbyArms(Player player) {
        super(player);
        setRepeatable(true);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "stubby_arms"+getUniqueNumber());


    private void addModifier() {
        AttributeModifier mod = new AttributeModifier(key, -2, AttributeModifier.Operation.ADD_NUMBER);
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
        getPlayer().getAttribute(Attribute.STEP_HEIGHT).removeModifier(key);
    }
}
