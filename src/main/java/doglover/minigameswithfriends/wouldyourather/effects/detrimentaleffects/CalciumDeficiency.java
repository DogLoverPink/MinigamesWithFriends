package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.attribute.Attribute;
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

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        player.getAttribute(Attribute.SAFE_FALL_DISTANCE).setBaseValue(0.5);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        player.getAttribute(Attribute.SAFE_FALL_DISTANCE).setBaseValue(3);
    }
}
