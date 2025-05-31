package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;

public class DaylightAllergy extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(DaylightAllergy.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You burn in the sun while not wearing a helmet";
    }

    public DaylightAllergy(Player player) {
        super(player);
        setRepeatable(false);

    }

    @Override
    public void on4HertzTick() {
        Player player = getPlayer();
        if (player.getLocation().getBlock().getLightFromSky() >= 15 &&
                player.getWorld().getTime() < 13000
                && !player.getWorld().hasStorm()) {
            if (player.getInventory().getHelmet() == null || player.getInventory().getHelmet().isEmpty()) {
                player.setFireTicks(20);
            } else {
                player.getInventory().getHelmet().damage(1, player);
            }
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
