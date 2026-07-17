package doglover.minigameswithfriends.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

public class TextUtils {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static String formatMaterialName(Material material) {
        String name = material.name().toLowerCase().replace('_', ' ');
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!formattedName.isEmpty()) {
                formattedName.append(" ");
            }
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                         .append(word.substring(1));
        }

        return formattedName.toString();
    }
}
