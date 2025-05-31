package doglover.minigameswithfriends.wouldyourather;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects.*;
import doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

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

    private static List<Class<? extends WYREffect>> TESTING_EFFECTS_TO_GET_FIRST = new ArrayList<>(List.of(Butterfinger.class));

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
                for  (Constructor<?> con : effectClass.getDeclaredConstructors()) {
                }
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
        MinigamesWithFriends.getGamePlugin().getLogger().info("Registered "+ beneficialEffects.size() +" beneficial effects!");
        MinigamesWithFriends.getGamePlugin().getLogger().info("Registered "+ detrimentialEffects.size() +" detrimental effects!");
    }

    public static List<WYREffect> managedEffects = new ArrayList<>();

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

    public static void tick() {
        tick4HertzCounter++;

        boolean do4Hz = tick4HertzCounter >= 5;

        for (WYREffect effect : managedEffects) {
            effect.onTick();
            if (do4Hz) {
                effect.on4HertzTick();
            }
        }
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
