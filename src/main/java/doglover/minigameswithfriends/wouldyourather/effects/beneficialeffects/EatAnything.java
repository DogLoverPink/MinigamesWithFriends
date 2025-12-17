package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EatAnything extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(EatAnything.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "eat";
    }

    public EatAnything(Player player) {
        super(player);
        setRepeatable(false);

    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        makeInventoryEdible();

    }

    @SuppressWarnings("UnstableApiUsage")
    public void makeInventoryEdible() {
        Player p = getPlayer();
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) {
                continue;
            }
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                continue;
            }
            if (item.hasData(DataComponentTypes.CONSUMABLE) || item.hasData(DataComponentTypes.BUNDLE_CONTENTS) || item.hasData(DataComponentTypes.BLOCKS_ATTACKS)) {
                continue;
            }
            FoodProperties food = FoodProperties
                    .food()
                    .canAlwaysEat(true)
                    .saturation(getSatiationAmount(item))
                    .nutrition((int) getSatiationAmount(item))
                    .build();
            item.setData(DataComponentTypes.FOOD, food);
            String soundString = "entity.generic.eat";
            if (item.getType().isBlock()) {
                NamespacedKey key = Registry.SOUNDS.getKey(item.getType().createBlockData().getSoundGroup().getBreakSound());
                if (key != null) {
                    soundString = key.asString();
                }
            }
            if (item.getType().name().toUpperCase().contains("IRON") || item.getType().name().toUpperCase().contains("GOLD") || item.getType().name().toUpperCase().contains("COPPER")) {
                soundString = "block.anvil.land";
            }
            Consumable a = Consumable.consumable().consumeSeconds(1.61f).sound(Key.key(soundString)).build();
            item.setData(DataComponentTypes.CONSUMABLE, a);
        }
    }

    private float getSatiationAmount(ItemStack item) {
        if (item.getType().isBlock()) {
            return 2.0f;
        } else if (item.getType().equals(Material.STICK)) {
            return 1.0f;
        }
        return 4.0f;
    }

    @Override
    public void on4HertzTick() {
        super.on4HertzTick();
        makeInventoryEdible();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
