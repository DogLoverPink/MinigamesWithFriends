package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.util.TriState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class SlipperyFloor extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(SlipperyFloor.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Slippery";
    }

    public SlipperyFloor(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        getPlayer().setFrictionState(TriState.FALSE);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
