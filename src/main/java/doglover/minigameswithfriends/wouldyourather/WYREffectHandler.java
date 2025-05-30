package doglover.minigameswithfriends.wouldyourather;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
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

    private static WYREffect getRandomEffectFromList(Player player, List<Class<? extends WYREffect>> classes) {
        Class<? extends WYREffect> effectClass = classes.get((int) (Math.random() * classes.size()));
        return constructWYREffectFromClass(effectClass, player);
    }

    private static WYREffect constructWYREffectFromClass(Class<? extends WYREffect> effectClass, Player player) {
        try {
            return effectClass.getDeclaredConstructor(Player.class).newInstance(player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void registerBeneficialWYREffect(Class<? extends WYREffect> effectClass) {
        beneficialEffects.add(effectClass);
    }

    public static void registerDetrimentalWYREffect(Class<? extends WYREffect> effectClass) {
        detrimentialEffects.add(effectClass);
    }
}
