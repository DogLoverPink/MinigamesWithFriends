package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class InvisibilityCloak extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(InvisibilityCloak.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Become invisible while sneaking";
    }

    public InvisibilityCloak(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerToggleSneakEvent.class);
    }

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player plr = event.getPlayer();
        if (!plr.equals(this.getPlayer())) {
            return;
        }
        plr.setInvisible(event.isSneaking());
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().setInvisible(false);
    }
}
