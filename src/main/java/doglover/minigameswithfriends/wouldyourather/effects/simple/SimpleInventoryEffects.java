package doglover.minigameswithfriends.wouldyourather.effects.simple;

import doglover.minigameswithfriends.utils.EnchantUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimpleInventoryEffects {
    static {
        WYREffectHandler.registerBeneficialWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "Have all of your gear randomly enchanted";
            }
            @Override
            public void onEffectInitiate() {
                enchantAtRandom(this.getPlayer());
            }}.getClass());

        WYREffectHandler.registerDetrimentalWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "You forgot how you sorted your inventory";
            }
            @Override
            public void onEffectInitiate() {
                shufflePlayerInventory(this.getPlayer());
            }}.getClass());

        WYREffectHandler.registerBeneficialWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "Everything is in your inventory is smelted";
            }
            @Override
            public void onEffectInitiate() {
                smeltAllInInventory(this.getPlayer());
            }}.getClass());

        WYREffectHandler.registerDetrimentalWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "Everything is in your inventory is smelted";
            }
            @Override
            public void onEffectInitiate() {
                smeltAllInInventory(this.getPlayer());
            }}.getClass());
    }

    public static void shufflePlayerInventory(Player player) {
        PlayerInventory inv = player.getInventory();

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack item = inv.getContents()[i];
            items.add(item == null ? null : item.clone());
        }

        Collections.shuffle(items);

        for (int i = 0; i < inv.getContents().length; i++) {
            inv.setItem(i, items.get(i));
        }
        player.updateInventory();
    }

    private static ItemStack cloneOrNull(ItemStack item) {
        return item == null ? null : item.clone();
    }

    private static void enchantAtRandom(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            EnchantUtils.enchantItemRandomly(item);
        }
    }


    public static void smeltAllInInventory(Player player) {
        player.sendMessage(player.getInventory().getContents().length + " items in inventory");
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack item = inv.getContents()[i];
            if (item == null || item.getType() == Material.AIR) continue;

            ItemStack result = getSmeltResult(item.getType());
            if (result != null) {
                ItemStack smelted = result.clone();
                smelted.setAmount(item.getAmount());
                player.getInventory().setItem(i, smelted);
            }
        }
        player.updateInventory();
    }

    private static ItemStack getSmeltResult(Material input) {
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe recipe = recipes.next();
            if (recipe instanceof FurnaceRecipe furnace) {
                if (furnace.getInput().getType() == input) {
                    return furnace.getResult();
                }
            }
        }
        return null;
    }
}
