package doglover.minigameswithfriends.wouldyourather;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects.*;
import doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static doglover.minigameswithfriends.wouldyourather.effects.simple.RemoveGoodOrBadEffect.removeRandomEffect;

public class WYREffectHandler {

    private static final List<Class<? extends WYREffect>> beneficialEffects = new ArrayList<>();
    private static final List<Class<? extends WYREffect>> detrimentialEffects = new ArrayList<>();

    public static WYREffect getRandomBeneficialWYREffectExceptFor(Player player, List<Class<? extends WYREffect>> exceptForClasses) {
        List<Class<? extends WYREffect>> effcopy = new ArrayList<>(beneficialEffects);
        effcopy.removeAll(exceptForClasses);
        return getRandomEffectFromList(player, effcopy);
    }

    public static WYREffect getRandomDetrimentialWYREffectExceptFor(Player player, List<Class<? extends WYREffect>> exceptForClasses) {
        List<Class<? extends WYREffect>> effcopy = new ArrayList<>(detrimentialEffects);
        effcopy.removeAll(exceptForClasses);
        return getRandomEffectFromList(player, effcopy);
    }

    private static List<Class<? extends WYREffect>> TESTING_EFFECTS_TO_GET_FIRST = new ArrayList<>(List.of(StreetInterviewer.class, ImmortalSnail.class));
    private static WYREffect getRandomEffectFromList(Player player, List<Class<? extends WYREffect>> classes) {
        if (!TESTING_EFFECTS_TO_GET_FIRST.isEmpty()) {
            return constructWYREffectFromClass(TESTING_EFFECTS_TO_GET_FIRST.removeFirst(), player);
        }
        Class<? extends WYREffect> effectClass = classes.get((int) (Math.random() * classes.size()));
        return constructWYREffectFromClass(effectClass, player);
    }


    private static WYREffect constructWYREffectFromClass(Class<? extends WYREffect> effectClass, Player player) {
        try {
            if (effectClass.isAnonymousClass()) {
                Constructor<? extends WYREffect> con = effectClass.getDeclaredConstructor();
                con.setAccessible(true);
                WYREffect eff = con.newInstance();
                eff.setPlayer(player);
                return eff;
            }
            return effectClass.getDeclaredConstructor(Player.class).newInstance(player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendEffectCounts() {
        Logger logger = MinigamesWithFriends.getGamePlugin().getLogger();
        logger.info("Registered "+ beneficialEffects.size() +" beneficial effects!");
        logger.info("Registered "+ detrimentialEffects.size() +" detrimental effects!");
    }

    public static boolean isEffectBeneficial(WYREffect effect) {
        return beneficialEffects.contains(effect.getClass());
    }

    public static boolean isEffectDetrimential(WYREffect effect) {
        return detrimentialEffects.contains(effect.getClass());
    }

    private static final List<WYREffect> managedEffects = new ArrayList<>();

    public static List<WYREffect> getManagedEffects() {
        return managedEffects;
    }

    public static void manageEffect(WYREffect effect) {
        managedEffects.add(effect);
    }

    public static void unmanageEffect(WYREffect effect) {
        managedEffects.remove(effect);
    }

    public static void clearAndDecomposeManagedEffects() {
        for (WYREffect effect : new ArrayList<>(managedEffects)) {
            effect.onEffectDecompose();
        }
        managedEffects.clear();
    }

    static int tick4HertzCounter = 0;

    private static final List<WYREffect> effectsToDecompose = new ArrayList<>();
    public static void decomposeEffectWhenSafe(WYREffect effect) {
        effectsToDecompose.add(effect);
    }

    public static void tick() {
        tick4HertzCounter++;

        boolean do4Hz = tick4HertzCounter >= 5;

        for (WYREffect effect : managedEffects) {
            if (!effect.isPlayerValid()) {
                continue;
            }
            effect.onTick();
            if (do4Hz) {
                effect.on4HertzTick();
            }
        }
        for (WYREffect effect : effectsToDecompose) {
            MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).removeEffectFromPlayer(effect.getPlayer(), effect.getClass());
            effect.onEffectDecompose();
        }
        effectsToDecompose.clear();
        if (do4Hz) {
            tick4HertzCounter = 0;
        }
    }


    public static void registerBeneficialWYREffect(Class<? extends WYREffect> effectClass) {
        beneficialEffects.add(effectClass);
    }

    public static void registerDetrimentalWYREffect(Class<? extends WYREffect> effectClass) {
        detrimentialEffects.add(effectClass);
    }


}
