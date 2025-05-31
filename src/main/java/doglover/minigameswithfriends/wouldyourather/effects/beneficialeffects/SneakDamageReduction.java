package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SneakDamageReduction extends WYREffect {

    static {
        WYREffectHandler.registerBeneficialWYREffect(SneakDamageReduction.class);
    }

    public SneakDamageReduction(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerToggleSneakEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }

    @Override
    public String getDescriptionBlurb() {
        return "Take less damage while sneaking";
    }


    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player plr = event.getPlayer();
        if (!plr.equals(this.getPlayer())) {
            return;
        }
        if (event.isSneaking()) {
            plr.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 1));
        } else {
            plr.removePotionEffect(PotionEffectType.RESISTANCE);
        }

    }
}
