package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoKnockback extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(NoKnockback.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Not take any knockback";
    }

    public NoKnockback(Player player) {
        super(player);
        setRepeatable(false);
    }

    NamespacedKey key = new NamespacedKey("minigameswithfriends", "no_knockback");

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        player.sendMessage("the clown");
       player.getAttribute(Attribute.KNOCKBACK_RESISTANCE).addModifier(new AttributeModifier(key, 3, AttributeModifier.Operation.ADD_NUMBER));

    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        player.getAttribute(Attribute.KNOCKBACK_RESISTANCE).removeModifier(key);
    }
}
