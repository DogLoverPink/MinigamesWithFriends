package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;

public class SmeltInventory extends WYREffect {

    static {
        WYREffectHandler.registerBeneficialWYREffect(SmeltInventory.class);

        WYREffectHandler.registerDetrimentalWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "EVERYTHING is in your inventory is smelted";
            }
            @Override
            public void onEffectInitiate() {
                smeltAllInInventoryBeneficial(this.getPlayer(), false);
            }}.getClass());
    }

    @Override
    public String getDescriptionBlurb() {
        return "Have your whole inventory smelted";
    }

    public SmeltInventory(Player player) {
        super(player);
    }

    @Override
    public void onEffectInitiate() {
        smeltAllInInventoryBeneficial(this.getPlayer(), true);
    }

    public static void smeltAllInInventoryBeneficial(Player player, boolean beneficial) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack item = inv.getContents()[i];
            if (item == null || item.getType() == Material.AIR) continue;

            ItemStack result = getSmeltResult(item.getType());
            String itemName = item.getType().name();
            if (result != null) {
                ItemStack smelted = result.clone();
                if (beneficial && smelted.getType().equals(Material.IRON_NUGGET)) {
                    continue;
                }
                smelted.setAmount(item.getAmount());
                player.getInventory().setItem(i, smelted);
            } else if (!beneficial) {
                if (itemName.contains("IRON")) {
                    ItemStack smelted = new ItemStack(Material.IRON_NUGGET);
                    smelted.setAmount(item.getAmount());
                    player.getInventory().setItem(i, smelted);
                } else if (itemName.contains("GOLD")) {
                    ItemStack smelted = new ItemStack(Material.GOLD_NUGGET);
                    smelted.setAmount(item.getAmount());
                    player.getInventory().setItem(i, smelted);
                } else if (itemName.contains("DIAMOND")) {
                    ItemStack smelted = new ItemStack(Material.DIAMOND);
                    smelted.setAmount(item.getAmount());
                    player.getInventory().setItem(i, smelted);
                } else if (itemName.contains("LOG") || itemName.contains("_WOOD")) {
                    ItemStack smelted = new ItemStack(Material.CHARCOAL);
                    smelted.setAmount(item.getAmount());
                    player.getInventory().setItem(i, smelted);
                }
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
