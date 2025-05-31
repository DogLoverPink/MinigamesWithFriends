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
        MinigamesWithFriends.getGamePlugin().getLogger().info("SneakWeakness registered");
    }

    public SneakWeakness(Player player) {
        super(player);
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
        return "You are weak when sneaking (test purposes)";
    }


    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player plr = event.getPlayer();
        if (!plr.equals(this.getPlayer())) {
            return;
        }
        if (event.isSneaking()) {
            plr.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, -1, 1));
        } else {
            plr.removePotionEffect(PotionEffectType.WEAKNESS);
        }

    }
}
