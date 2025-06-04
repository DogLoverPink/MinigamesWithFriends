package doglover.minigameswithfriends.wouldyourather.effects.simple;

import doglover.minigameswithfriends.utils.EnchantUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;

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
                this.selfDestruct();
            }
        }.getClass());

        WYREffectHandler.registerDetrimentalWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "You forgot how you sorted your inventory";
            }

            @Override
            public void onEffectInitiate() {
                shufflePlayerInventory(this.getPlayer());
                this.selfDestruct();
            }
        }.getClass());

        WYREffectHandler.registerBeneficialWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "All of your gear gets upgraded";
            }

            @Override
            public void onEffectInitiate() {
                upgradeGear(this.getPlayer());
                this.selfDestruct();
            }
        }.getClass());

        WYREffectHandler.registerBeneficialWYREffect(new WYREffect() {
            @Override
            public String getDescriptionBlurb() {
                return "Fill your inventory with random items";
            }

            @Override
            public void onEffectInitiate() {
                fillInventoryWithRandomItems(getPlayer());
                this.selfDestruct();
            }
        }.getClass());

    }


    private static void fillInventoryWithRandomItems(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                Material randomMaterial = Material.values()[new Random().nextInt(Material.values().length)];
                int amount = (int) (Math.random() * 5) + 1;
                if (randomMaterial.isItem()) {
                    inv.setItem(i, new ItemStack(randomMaterial, amount));
                }
            }
        }
        player.updateInventory();
    }

    private static final Map<String, String> upgradePath = Map.of(
            "WOODEN", "STONE",
            "STONE", "IRON",
            "GOLDEN", "IRON",
            "IRON", "DIAMOND",
            "CHAINMAIL", "DIAMOND",
            "LEATHER", "GOLDEN",
            "DIAMOND", "NETHERITE");

    public static void upgradeGear(Player player) {
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            String materialName = item.getType().name();
            String[] splitName = materialName.split("_");
            String nextMaterialName = upgradePath.get(splitName[0]);
            if (nextMaterialName == null || splitName.length < 2) {
                continue;
            }
            Material nextMaterial = Material.getMaterial(nextMaterialName + "_" + splitName[1]);

            if (nextMaterial == null) {
                continue;
            }
            ItemStack upgradedItem = new ItemStack(nextMaterial, item.getAmount());
            upgradedItem.setItemMeta(item.getItemMeta());
            player.getInventory().setItem(i, upgradedItem);
        }

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

}
