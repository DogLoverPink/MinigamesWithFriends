package doglover.minigameswithfriends.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EnchantUtils {

    private static final Random random = new Random();

    public static void enchantItemRandomly(ItemStack item) {
        List<Enchantment> applicable = new ArrayList<>();
        for (Enchantment enchant : Enchantment.values()) {
            if (enchant.canEnchantItem(item)) {
                applicable.add(enchant);
            }
        }

        Collections.shuffle(applicable, random);
        int numEnchantments = 1 + random.nextInt(3);

        for (int i = 0; i < Math.min(numEnchantments, applicable.size()); i++) {
            Enchantment enchant = applicable.get(i);
            int level = 1 + random.nextInt(5);
            item.addUnsafeEnchantment(enchant, level);
        }
    }
}
