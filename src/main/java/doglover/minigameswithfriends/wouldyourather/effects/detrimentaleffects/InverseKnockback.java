package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class InverseKnockback extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(InverseKnockback.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Knockback you take is inversed";
    }

    public InverseKnockback(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityKnockbackEvent.class);
        subscribeToEvent(EntityKnockbackByEntityEvent.class);
    }

    @Override
    public void onEntityKnockback(EntityKnockbackEvent event) {
        event.getEntity().sendMessage("meow");
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        event.setKnockback(event.getKnockback().multiply(-1));
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
