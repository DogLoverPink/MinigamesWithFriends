package doglover.minigameswithfriends.wouldyourather.effects.beneficialEfffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SneakDamageReduction extends WYREffect {

    public SneakDamageReduction(Player player) {
        super(player);
        subscribeToEvent(PlayerToggleSneakEvent.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You take less damage while sneaking";
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
