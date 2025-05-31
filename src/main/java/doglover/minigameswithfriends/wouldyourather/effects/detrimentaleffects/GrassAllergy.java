package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class GrassAllergy extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(GrassAllergy.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You're deathly allergic to grass";
    }

    public GrassAllergy(Player player) {
        super(player);
        setRepeatable(false);
    }

    @Override
    public void onTick() {
        Player player = getPlayer();
        if (player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType().toString().contains("GRASS")) {
            player.damage(2);
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
