package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class AttackFastWithShovel extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(AttackFastWithShovel.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Have no cooldown when attacking with a wooden shovel";
    }

    public AttackFastWithShovel(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityDamageByEntityEvent.class);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "fastshovelattack" + getUniqueNumber());

    @Override
    public void onTick() {
        ItemStack item = getPlayer().getInventory().getItemInMainHand();
        if (item.getType() != Material.WOODEN_SHOVEL) {
            removeAttribute();
            return;
        }
        AttributeModifier mod = new AttributeModifier(key, 5, AttributeModifier.Operation.ADD_NUMBER);
        if (getPlayer().getAttribute(Attribute.ATTACK_SPEED).getModifier(key) == null) {
            getPlayer().getAttribute(Attribute.ATTACK_SPEED).addModifier(mod);
        }
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getDamager().equals(getPlayer())) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }
        if (getPlayer().getInventory().getItemInMainHand().getType() != Material.WOODEN_SHOVEL) {
            return;
        }
        victim.setNoDamageTicks(0);
        Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> victim.setNoDamageTicks(0), 1);
    }

    private void removeAttribute() {
        getPlayer().getAttribute(Attribute.ATTACK_SPEED).removeModifier(key);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        removeAttribute();
    }
}
