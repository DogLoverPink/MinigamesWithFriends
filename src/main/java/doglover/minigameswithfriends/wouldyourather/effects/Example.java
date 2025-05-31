package doglover.minigameswithfriends.wouldyourather.effects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Example extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(Example.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "";
    }

    public Example(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
