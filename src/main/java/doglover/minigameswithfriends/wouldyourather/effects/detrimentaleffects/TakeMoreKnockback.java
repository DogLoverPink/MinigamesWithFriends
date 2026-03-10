package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TakeMoreKnockback extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(TakeMoreKnockback.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Knockback you take is increased";
    }

    public TakeMoreKnockback(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityKnockbackEvent.class);
        subscribeToEvent(EntityKnockbackByEntityEvent.class);
    }

//    @Override
//    public void onEntityKnockback(EntityKnockbackEvent event) {
//        if (!event.getEntity().equals(getPlayer())) {
//            return;
//        }
//        getPlayer().sendMessage("meow1");
//        Vector knockBack = event.getKnockback();
//        knockBack = knockBack.multiply(new Vector(6.25, 1.6, 6.25));
//        event.setKnockback(knockBack);
//    }

    @Override
    public void onEntityKnockbackByEntity(EntityKnockbackByEntityEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        Vector knockBack = event.getKnockback();
        knockBack = knockBack.multiply(new Vector(3.5, 1.25, 3.5));
//        knockBack = knockBack.multiply(2);
        event.setKnockback(knockBack);

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
