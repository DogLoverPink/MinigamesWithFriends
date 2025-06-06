package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class Shorty extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(Shorty.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "bro ain't even 6ft ☠☠☠";
    }

    public Shorty(Player player) {
        super(player);
        setRepeatable(true);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "shorty_"+getUniqueNumber());


    private void addModifier() {
        AttributeModifier mod = new AttributeModifier(key, -0.3, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.SCALE).addModifier(mod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        addModifier();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.SCALE).removeModifier(key);
    }
}
