package doglover.minigameswithfriends.utils;

import java.util.Random;

public class NumberUtils {

    public static Random random = new Random();

    public static boolean chanceOf(double chance) {
        return Math.random() < chance;
    }

    public static String formatTimeStamp(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);
        return minutes + ":" + String.format("%02d", seconds);
    }
}
