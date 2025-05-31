package doglover.minigameswithfriends.utils;

public class NumberUtils {

    public static boolean chanceOf(double chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("Chance must be between 0 and 1");
        }
        return Math.random() < chance;
    }
}
