package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class InverseFallDamage extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(InverseFallDamage.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Heal and temporarily overheal every time you would take fall damage";
    }

    public InverseFallDamage(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityDamageEvent.class);
    }

    @Override
    public void onTick() {
        if (mod == null || player == null || !player.isOnline()) {
            return;
        }
        if (System.currentTimeMillis() - lastFallTime > 3000 && player.getAbsorptionAmount() > 4) {
            player.setAbsorptionAmount(player.getAbsorptionAmount() - 0.1);
            if (player.getAbsorptionAmount() <= 0) {
                player.getAttribute(Attribute.MAX_ABSORPTION).removeModifier(mod);
            }
        }

    }

    long lastFallTime = 0;

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.getEntity().equals(getPlayer()) || event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        double damage = event.getDamage();
        event.setCancelled(true);
        double overhealAmount = damage - getMaxHealthDifference();
        player.heal(damage);
        if (overhealAmount <= 0) {
            return;
        }
        overhealAmount += player.getAbsorptionAmount();
        overhealAmount = Math.min(20, overhealAmount); // Cap at 20 (max absorption)
        lastFallTime = System.currentTimeMillis();

        mod = new AttributeModifier(new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "assorption"), overhealAmount, AttributeModifier.Operation.ADD_NUMBER);
        player.getAttribute(Attribute.MAX_ABSORPTION).removeModifier(mod);
        player.getAttribute(Attribute.MAX_ABSORPTION).addModifier(mod);
        player.setAbsorptionAmount(overhealAmount);
    }

    AttributeModifier mod;

    private double getMaxHealthDifference() {
        double maxHealth = player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        double currentHealth = player.getHealth();
        double difference = maxHealth - currentHealth;
        return difference > 0 ? difference : 0;
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
