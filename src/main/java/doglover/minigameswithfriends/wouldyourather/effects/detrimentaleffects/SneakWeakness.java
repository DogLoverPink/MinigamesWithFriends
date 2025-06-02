package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SneakWeakness extends WYREffect {

    static {
        WYREffectHandler.registerDetrimentalWYREffect(SneakWeakness.class);
    }

    public SneakWeakness(Player player) {
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
        getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You can't attack while sneaking";
    }


    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player plr = event.getPlayer();
        if (!plr.equals(this.getPlayer())) {
            return;
        }
        if (event.isSneaking()) {
            plr.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, -1, 9, false, false, false));
        } else {
            plr.removePotionEffect(PotionEffectType.WEAKNESS);
        }

    }
}
