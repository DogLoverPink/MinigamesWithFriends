package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import com.google.common.collect.Lists;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PickRandomItem extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(PickRandomItem.class);
    }

    List<Enchantment> enchantmentList;

    List<Material> matList;
    Inventory inventory = null;

    boolean isPicking = false;

    @Override
    public String getDescriptionBlurb() {
        return "Get to pick from a fortuitous selection of goodies";
    }

    public PickRandomItem(Player player) {
        super(player);

        setRepeatable(true);
        subscribeToEvent(InventoryCloseEvent.class);
        subscribeToEvent(InventoryClickEvent.class);

        enchantmentList = Lists.newArrayList(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).iterator());
        List<String> names = List.of("iron", "gold", "diamond", "netherite", "shell", "mace");
        List<Material> matList = new ArrayList<>(Arrays.asList(Material.values()));
        matList.removeIf(item -> {
            String matName = item.toString();
            for (String word: names) {
                if (!item.isItem()) {
                    return true;
                }
                if (matName.toLowerCase().contains(word)) {
                    return false;
                }
            }
            return true;
        });
        this.matList = matList;

    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != getPlayer()) {
            return;
        }
        if (!inventory.equals(event.getClickedInventory())) {
            return;
        }
        if (isPicking) {
            ItemStack item = event.getCurrentItem();
            if (item == null) {
                return;
            }
            ItemUtils.giveItemsToPlayer(getPlayer(), item);
            event.setCancelled(true);
            isPicking = false;
            inventory.close();
            getPlayer().sendMessage(Component.text("§aYou picked §b" + item.getAmount() + " §e" + item.getType() + "§a!"));
            this.selfDestruct();
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() != getPlayer()) {
            return;
        }
        if (isPicking) {
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> getPlayer().openInventory(inventory), 1);

        }
    }

    public void showGuiToPlayer() {
        inventory = Bukkit.createInventory(null, 27, Component.text("Pick One!"));
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = new ItemStack(matList.get(random.nextInt(0, matList.size())), 1);
            int amount = random.nextInt(1, item.getMaxStackSize() + 1);
            item.setAmount(amount);
            randomlyEnchantItemStack(item);
            inventory.setItem(i, item);
        }
        getPlayer().openInventory(inventory);
        isPicking = true;
        Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
            if (isPicking) {
                isPicking = false;
                inventory.close();
                getPlayer().sendMessage(Component.text("§cYou ran out of time! Picking one at random..."));
                ItemStack randItem = inventory.getItem(random.nextInt(inventory.getSize()));
                ItemUtils.giveItemsToPlayer(getPlayer(), randItem);
                this.selfDestruct();

            }

        }, 100);


    }

    public void randomlyEnchantItemStack(ItemStack itemStack) {
                int level = random.nextInt(1, 6); // Random enchantment level between 1 and 5
                Enchantment enchantment = enchantmentList.get(random.nextInt(enchantmentList.size()));

            itemStack.addUnsafeEnchantment(enchantment, level);


    }



    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        showGuiToPlayer();
    }

    @Override
    public void onEffectDecompose() {
        if (inventory != null) {
            inventory.close();
        }
        super.onEffectDecompose();
    }
}

