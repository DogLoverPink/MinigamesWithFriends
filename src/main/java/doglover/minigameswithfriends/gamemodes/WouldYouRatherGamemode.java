package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.JarUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WouldYouRatherGamemode extends TimeEventBasedGamemode {

    public final Map<UUID, List<Class<? extends WYREffect>>> currentlyAppliedBenefitsAndDetriments = new HashMap<>();


    public static void initialize() {
        JarUtils.initalizeWouldYouRatherClasses("doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects");
        JarUtils.initalizeWouldYouRatherClasses("doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects");
        JarUtils.initalizeWouldYouRatherClasses("doglover.minigameswithfriends.wouldyourather.effects.simple");
        WYREffectHandler.sendEffectCounts();
    }

    public boolean playerHasEffect(Player plr, Class<? extends WYREffect> effectClass) {
        return currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).contains(effectClass);
    }



    public void removeEffectFromPlayer(Player plr, Class<? extends WYREffect> effectClass) {
        currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).remove(effectClass);
    }

    @Override
    public void tick() {
        super.tick();
        if (!effectsToChooseFrom.isEmpty()) {
            getGame().addScoreboardContribution("§bTime to make choice: §d" + getFormattedTimeRemaining());
        }
        WYREffectHandler.tick();

    }

    private void chooseOptionOne(Audience aud) {
        UUID uuid = aud.get(Identity.UUID).get();
        List<WYREffect> effects = effectsToChooseFrom.get(uuid);
        if (effects == null || effects.isEmpty() || Bukkit.getPlayer(uuid) == null || !getGame().isRunning()) {
            return;
        }
        applyEffects(Bukkit.getPlayer(uuid), effects.get(0), effects.get(1));
    }


    private void chooseOptionTwo(Audience aud) {
        UUID uuid = aud.get(Identity.UUID).get();
        List<WYREffect> effects = effectsToChooseFrom.get(uuid);
        if (effects == null || effects.isEmpty() || Bukkit.getPlayer(uuid) == null || !getGame().isRunning())  {
            return;
        }
        applyEffects(Bukkit.getPlayer(uuid), effects.get(2), effects.get(3));
    }

    private void applyEffects(Player plr, WYREffect goodEffect, WYREffect badEffect) {
        if (!goodEffect.isRepeatable()) {
            currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).add(goodEffect.getClass());
        }
        if (!badEffect.isRepeatable()) {
            currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).add(badEffect.getClass());
        }
        plr.sendMessage(Component.text("You chose!"));
        goodEffect.onEffectInitiate();
        badEffect.onEffectInitiate();
        effectsToChooseFrom.remove(plr.getUniqueId());
        if (effectsToChooseFrom.isEmpty()) {
            endChoiceChooseTimer();
        }

    }

    boolean isCurrentChoosing = false;

    public boolean isCurrentChoosing() {
        return isCurrentChoosing;
    }

    public boolean isPlayerCurrentlyChoosing(Player plr) {
        return effectsToChooseFrom.containsKey(plr.getUniqueId());
    }

    public Map<UUID, List<WYREffect>> effectsToChooseFrom = new HashMap<>();

    @Override
    public void onTimeEventTrigger() {
        isCurrentChoosing = true;
        for (Player plr : getGame().getPlayers()) {
            plr.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 4));
            List<Class<? extends WYREffect>> current = currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId());
            effectsToChooseFrom.putIfAbsent(plr.getUniqueId(), new ArrayList<>());
            WYREffect good1 = WYREffectHandler.getRandomBeneficialWYREffectExceptFor(plr, current);
            WYREffect bad1 = WYREffectHandler.getRandomDetrimentialWYREffectExceptFor(plr, current);
            WYREffect good2 = WYREffectHandler.getRandomBeneficialWYREffectExceptFor(plr, current);
            WYREffect bad2 = WYREffectHandler.getRandomDetrimentialWYREffectExceptFor(plr, current);
            sendWYRIntro(plr);
            sendWYREffectChoice(plr, good1, bad1, 1);
            plr.sendMessage(Component.empty());
            plr.sendMessage(Component.text("OR").appendNewline().color(NamedTextColor.YELLOW));
            sendWYREffectChoice(plr, good2, bad2, 2);
            sendWYREnd(plr);
            effectsToChooseFrom.get(plr.getUniqueId()).addAll(Arrays.asList(good1, bad1, good2, bad2));
        }
        runChoiceChooseTimer();
    }

    private void runChoiceChooseTimer() {
        setTickGoal(20 * getGame().getConfig().getWouldYouRatherConfig().getAllocatedSecondsForChoosingOption());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (effectsToChooseFrom.isEmpty()) {
                    cancel();
                }
                if (getTickGoal() > 3) {
                    return;
                }
                endChoiceChooseTimer();
                cancel();
            }
        }.runTaskTimer(MinigamesWithFriends.getGamePlugin(), 0, 1);
    }

    private void endChoiceChooseTimer() {
        setTickGoal(getNextComputedTime());
        List<UUID> uuids = new ArrayList<>(effectsToChooseFrom.keySet());
        for (UUID uuid : uuids) {
            Player plr = Bukkit.getPlayer(uuid);
            plr.closeInventory();
            plr.sendMessage(Component.text("You ran out of time! Automatically choosing first option... ").color(NamedTextColor.RED));
            chooseOptionOne(plr);
        }
        for (Player plr: getGame().getPlayers()) {
            plr.removePotionEffect(PotionEffectType.RESISTANCE);
        }
        isCurrentChoosing = false;
    }

    private void sendWYRIntro(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW).appendNewline();
        message = message.append(Component.text("Would you rather: ").color(NamedTextColor.AQUA));
        plr.sendMessage(message);
    }

    private void sendWYREnd(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW);
        plr.sendMessage(message);
    }


    private void sendWYREffectChoice(Player plr, WYREffect goodEffect, WYREffect badEffect, int option1or2) {
        ClickCallback<Audience> callback = option1or2 == 1 ? this::chooseOptionOne : this::chooseOptionTwo;
        Component message = Component.text(" " + goodEffect.getDescriptionBlurb()).color(NamedTextColor.GREEN).appendNewline();
        message = message.append(Component.text("BUT...").color(NamedTextColor.YELLOW).appendNewline());
        message = message.append(Component.text(badEffect.getDescriptionBlurb()).color(NamedTextColor.RED)).appendNewline();
        message = message.append(
                Component.text("[SELECT]")
                        .clickEvent(ClickEvent.callback(callback))
                        .hoverEvent(Component.text("Choose option " + option1or2).color(NamedTextColor.GREEN)));
        plr.sendMessage(message);
    }


    @Override
    public void onGameEnd() {
        WYREventHandler.setActive(false);
        currentlyAppliedBenefitsAndDetriments.clear();
        WYREffectHandler.clearAndDecomposeManagedEffects();
        for (Player plr : getGame().getPlayers()) {
            for (PotionEffect effect : plr.getActivePotionEffects()) {
                plr.removePotionEffect(effect.getType());
            }
            plr.removePotionEffect(PotionEffectType.RESISTANCE);
        }

    }

    @Override
    public void onGameStart() {
        WYREventHandler.setActive(true);
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
