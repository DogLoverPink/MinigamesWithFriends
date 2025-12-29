package doglover.minigameswithfriends.wouldyourather.effects.simple;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class RemoveGoodOrBadEffect {

    static {
        WYREffectHandler.registerBeneficialWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "Remove a random WYR detriment";
            }

            @Override
            public void onEffectInitiate() {
                removeRandomEffect(getPlayer(), false);
                this.selfDestruct();
            }
        }.getClass());


        WYREffectHandler.registerDetrimentalWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "Remove a random WYR beneficial effect";
            }

            @Override
            public void onEffectInitiate() {
                removeRandomEffect(getPlayer(), true);
                this.selfDestruct();
            }
        }.getClass());
    }

    static final Random random = new Random();

    public static void removeRandomEffect(Player player, boolean removeBeneficial) {
        List<WYREffect> effects = new ArrayList<>(WYREffectHandler.getManagedEffects());
        Predicate<WYREffect> goodOrBadChecker = removeBeneficial ? WYREffectHandler::isEffectDetrimential : WYREffectHandler::isEffectBeneficial;
        effects.removeIf(goodOrBadChecker);
        effects.removeIf(effect -> effect.getClass().isAnonymousClass());
        effects.removeIf(effect -> !effect.getPlayer().equals(player));
        if (effects.isEmpty()) {
            return;
        }
        WYREffect randomEffect = effects.get(random.nextInt(effects.size()));
        randomEffect.selfDestruct();
        TextColor color = removeBeneficial ? NamedTextColor.RED : NamedTextColor.GREEN;
        player.sendMessage(Component.text("Removed " + randomEffect.getDescriptionBlurb()).color(color));
    }

}
