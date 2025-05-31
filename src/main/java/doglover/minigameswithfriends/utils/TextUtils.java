package doglover.minigameswithfriends.utils;

import org.bukkit.Material;

public class TextUtils {
    public static String formatMaterialName(Material material) {
        String name = material.name().toLowerCase().replace('_', ' ');
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (formattedName.length() > 0) {
                formattedName.append(" ");
            }
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                         .append(word.substring(1));
        }

        return formattedName.toString();
    }
}
