package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RideAnything extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(RideAnything.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to ride anything by right clicking with an empty hand";
    }

    public RideAnything(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEntityEvent.class);
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (!getPlayer().getInventory().getItemInMainHand().isEmpty()) {
            return;
        }
        Entity entity = getTopEntity(event.getRightClicked());
        entity.addPassenger(getPlayer());
    }

    private Entity getTopEntity(Entity entity) {
        if (entity.getPassengers().isEmpty()) {
            return entity;
        }
        else return getTopEntity(entity.getPassengers().getFirst());
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
