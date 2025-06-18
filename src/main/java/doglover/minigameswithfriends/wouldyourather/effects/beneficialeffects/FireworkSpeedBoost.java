package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;


public class FireworkSpeedBoost extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(FireworkSpeedBoost.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Have fireworks always boost you even without an elytra";
    }

    public FireworkSpeedBoost(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().equals(getPlayer()) ||event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (getPlayer().isGliding() || event.getItem() == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item.getType() != Material.FIREWORK_ROCKET || !(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        FireworkMeta meta = (FireworkMeta) item.getItemMeta();
        double power = 1.5;
        if (meta.hasPower()) {
            power += meta.getPower() / 2.0;
        }
        Vector velocity = event.getPlayer().getLocation().getDirection().multiply(power);
        velocity = velocity.add(new Vector(0, getPlayer().getVelocity().getY(), 0));

        getPlayer().setVelocity(velocity);
        getPlayer().setGliding(true);
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
