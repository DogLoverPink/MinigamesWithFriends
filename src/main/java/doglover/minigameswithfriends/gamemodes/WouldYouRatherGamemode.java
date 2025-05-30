package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.JarUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.*;

public class WouldYouRatherGamemode extends TimeEventBasedGamemode {

    private Map<UUID, List<Class<? extends WYREffect>>> currentlyAppliedBenefitsAndDetriments = new HashMap<>();


    public static void initialize() {
        JarUtils.initalizeWouldYouRatherClasses("doglover.minigameswithfriends.wouldyourather.effects.beneficialefffects");
        JarUtils.initalizeWouldYouRatherClasses("doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects");

    }

    @Override
    public void tick() {
        super.tick();

    }

    private void test(Audience aud) {
        aud.sendMessage(Component.text("meow"));
        MinigamesWithFriends.getGamePlugin().getLogger().info("uuid: " + aud.get(Identity.UUID).get());

    }

    @Override
    public void onTimeEventTrigger() {
        for (Player plr : getGame().getPlayers()) {
            List<Class<? extends WYREffect>> current = currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId());
            WYREffect good1 = WYREffectHandler.getRandomBeneficialWYREffectExceptFor(plr, current);
            WYREffect bad1 = WYREffectHandler.getRandomDetrimentialWYREffectExceptFor(plr, current);
            WYREffect good2 = WYREffectHandler.getRandomBeneficialWYREffectExceptFor(plr, current);
            WYREffect bad2 = WYREffectHandler.getRandomDetrimentialWYREffectExceptFor(plr, current);
            sendWYRIntro(plr);
            sendWYREffectChoice(plr, good1, bad1);
            plr.sendMessage(Component.text("OR").color(NamedTextColor.YELLOW));
            sendWYREffectChoice(plr, good2, bad2);
            sendWYREnd(plr);
        }
    }

    private void sendWYRIntro(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW);
        message = message.append(Component.text("Would you rather: ").color(NamedTextColor.AQUA));
        plr.sendMessage(message);
    }

    private void sendWYREnd(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW);
        plr.sendMessage(message);
    }


    private void sendWYREffectChoice(Player plr, WYREffect goodEffect, WYREffect badEffect) {
        Component message = Component.text(goodEffect.getDescriptionBlurb()).color(NamedTextColor.GREEN);
        message = message.append(Component.text("BUT...").color(NamedTextColor.YELLOW));
        message = message.append(Component.text(badEffect.getDescriptionBlurb()).color(NamedTextColor.RED));
        message = message.append(
                Component.text("[CHOOSE]")
                        .clickEvent(ClickEvent.callback(this::test))
                        .hoverEvent(Component.text("Hover me option 1!")));
        plr.sendMessage(message);
    }


    @Override
    public void onGameEnd() {

    }

    @Override
    public void onGameStart() {
        this.setMinTicks(getGame().getConfig().getWouldYouRatherConfig().getMinimumSecondsBeforeNewChoice() * 20);
        this.setMaxTicks(getGame().getConfig().getWouldYouRatherConfig().getMaximumSecondsBeforeNewChoice() * 20);
        super.onGameStart();
        for (Player plr : this.getGame().getPlayers()) {
            currentlyAppliedBenefitsAndDetriments.put(plr.getUniqueId(), new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return "WouldYouRather";
    }
}
