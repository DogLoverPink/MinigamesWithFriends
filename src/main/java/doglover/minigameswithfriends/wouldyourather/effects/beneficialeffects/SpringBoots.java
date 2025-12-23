package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpringBoots extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(SpringBoots.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to jump higher by holding sneak";
    }

    public SpringBoots(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerJumpEvent.class);
    }


    int sneakCounter = 0;

    @Override
    public void onPlayerJump(PlayerJumpEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (sneakCounter > 0) {
            getPlayer().setVelocity(new Vector(0, sneakCounter / 45.0, 0));
            sneakCounter = 0;

        }
    }

    @Override
    public void onTick() {
        if (getPlayer().isSneaking()) {
            sneakCounter++;
            if (sneakCounter >= 10 && sneakCounter % 10 == 0) {
                float pitch = (sneakCounter + 15) / 75f;
                getPlayer().playSound(getPlayer(), Sound.BLOCK_LEVER_CLICK, 0.25f, pitch);
            }
        } else {
            sneakCounter = 0;
        }
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
