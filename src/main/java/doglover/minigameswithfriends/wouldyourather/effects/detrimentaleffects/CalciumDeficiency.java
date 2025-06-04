package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class CalciumDeficiency extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(CalciumDeficiency.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You have a calcium deficiency";
    }

    public CalciumDeficiency(Player player) {
        super(player);
        setRepeatable(false);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "calcium_deficiency");

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        AttributeModifier mod = new AttributeModifier(key, 2.5, AttributeModifier.Operation.ADD_NUMBER);
        player.getAttribute(Attribute.SAFE_FALL_DISTANCE).addModifier(mod);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        player.getAttribute(Attribute.SAFE_FALL_DISTANCE).removeModifier(key);
    }
}
