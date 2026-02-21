package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        subscribeToEvent(PlayerDeathEvent.class, EventPriority.MONITOR);
    }

    int respawnProtection = 0;

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (!event.isCancelled()) {
            respawnProtection = 16;
        }
    }

    @Override
    public void on4HertzTick() {
        if (respawnProtection > 0) {
            respawnProtection--;
            return;
        }
        Player player = getPlayer();
        if (player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType().toString().contains("GRASS")) {
            player.damage(3);
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
