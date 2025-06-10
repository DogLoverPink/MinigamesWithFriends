package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Butterfinger extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(Butterfinger.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Your fingers become made of butter";
    }

    public Butterfinger(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    @Override
    public void on4HertzTick() {
        if (!NumberUtils.chanceOf(0.011) || getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
            return;
        }
        Vector dropVector = getPlayer().getLocation().getDirection().multiply(0.35);
        Item item = getPlayer().getWorld().dropItem(getPlayer().getLocation().clone().add(0, 1, 0), getPlayer().getInventory().getItemInMainHand());
        item.setVelocity(dropVector);
        item.setPickupDelay(20);
        getPlayer().getInventory().setItemInMainHand(null);

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
