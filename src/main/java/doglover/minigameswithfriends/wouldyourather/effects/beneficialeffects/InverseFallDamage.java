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
        if (mod == null || getPlayer() == null || !getPlayer().isOnline()) {
            return;
        }
        if (System.currentTimeMillis() - lastFallTime > 3000 && getPlayer().getAbsorptionAmount() > 4) {
            getPlayer().setAbsorptionAmount(getPlayer().getAbsorptionAmount() - 0.1);
            if (getPlayer().getAbsorptionAmount() <= 0) {
                getPlayer().getAttribute(Attribute.MAX_ABSORPTION).removeModifier(mod);
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
        getPlayer().heal(damage);
        if (overhealAmount <= 0) {
            return;
        }
        overhealAmount += getPlayer().getAbsorptionAmount();
        overhealAmount = Math.min(20, overhealAmount); // Cap at 20 (max absorption)
        lastFallTime = System.currentTimeMillis();

        mod = new AttributeModifier(new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "assorption"), overhealAmount, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.MAX_ABSORPTION).removeModifier(mod);
        getPlayer().getAttribute(Attribute.MAX_ABSORPTION).addModifier(mod);
        getPlayer().setAbsorptionAmount(overhealAmount);
    }

    AttributeModifier mod;

    private double getMaxHealthDifference() {
        double maxHealth = getPlayer().getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        double currentHealth = getPlayer().getHealth();
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
        getPlayer().setAbsorptionAmount(0.0);
    }
}
