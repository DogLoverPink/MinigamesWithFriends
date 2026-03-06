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
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.crypto.Data;

public class EatAnything extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(EatAnything.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to eat anything";
    }

    public EatAnything(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerItemHeldEvent.class);

    }

    @Override
    //Not necesary, but just speeds it up
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        ItemStack newItem = getPlayer().getInventory().getItem(event.getNewSlot());
        if (newItem == null) return;
        getPlayer().getInventory().setItem(event.getNewSlot(), makeItemEdible(newItem));
    }

    @Override
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        event.getItem().setItemStack(makeItemEdible(event.getItem().getItemStack()));
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();

    }

    @Override
    public void on4HertzTick() {
        super.on4HertzTick();
        for (int i = 0; i < getPlayer().getInventory().getSize(); i++) {
            ItemStack item = getPlayer().getInventory().getItem(i);
            if (item == null) {
                continue;
            }
            getPlayer().getInventory().setItem(i, makeItemNotEdible(item));
        }
        //Can't fully detect if inventory is open, so this is the best possible way
        if (getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING) {
            ItemStack itemInMainHand = getPlayer().getInventory().getItemInMainHand();
            getPlayer().getInventory().setItemInMainHand(makeItemEdible(itemInMainHand));
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private ItemStack makeItemNotEdible(ItemStack item) {
        if (!item.hasData(DataComponentTypes.FOOD) || item.getType().isEdible()) {
            return item;
        }
        item.unsetData(DataComponentTypes.CONSUMABLE);
        item.unsetData(DataComponentTypes.FOOD);
        return item;
    }

    @SuppressWarnings("UnstableApiUsage")
    private ItemStack makeItemEdible(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (item.hasData(DataComponentTypes.CONSUMABLE) || item.hasData(DataComponentTypes.BUNDLE_CONTENTS) || item.hasData(DataComponentTypes.BLOCKS_ATTACKS)) {
            return item;
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
        return item;
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
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
