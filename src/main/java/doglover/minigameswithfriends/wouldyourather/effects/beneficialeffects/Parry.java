package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import com.destroystokyo.paper.MaterialTags;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Parry extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(Parry.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to parry attacks by rightclicking with a sword";
    }

    public Parry(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEvent.class);
        subscribeToEvent(EntityDamageByEntityEvent.class);
    }

    int parryTime = 0;

    @Override
    public void onTick() {
        if (parryTime > 0) {
            parryTime--;
            if (parryTime == 0) {
                parryTime = -10;
            }
        }
        if (parryTime < 0) {
            parryTime++;
        }
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        if (parryTime <= 0) {
            return;
        }
        event.setCancelled(true);
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0.8f);
        parryTime = -40;
        if (!(event.getDamager() instanceof LivingEntity damager)) {
            return;
        }
        damager.damage(event.getDamage(), getPlayer());
    }


    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
        if (parryTime != 0) {
            return;
        }
        ItemStack handItem = getPlayer().getInventory().getItemInMainHand();
        if (handItem.isEmpty() || !MaterialTags.SWORDS.isTagged(handItem.getType())) {
            return;
        }
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
        parryTime = 7;
        getPlayer().setCooldown(handItem.getType(), parryTime);
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
