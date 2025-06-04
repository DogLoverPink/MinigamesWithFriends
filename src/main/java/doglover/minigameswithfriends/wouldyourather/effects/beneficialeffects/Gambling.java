package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Gambling extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(Gambling.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "GAMBLING!!!! WOOHOO BABY!!";
    }

    Inventory inv;

    public Gambling(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(InventoryCloseEvent.class);
        subscribeToEvent(InventoryClickEvent.class);
    }

    boolean isGambling;
    int spinTime = 1;
    int tickCounter = 0;

    @Override
    public void onTick() {
        if (inv == null || !isGambling || !getPlayer().isOnline()) {
            return;
        }
        tickCounter++;
        if (tickCounter > spinTime) {
            tickCounter = 0;
            setSlotsToItems();
            spinTime++;
        }
        if (spinTime > 18) {
            for (ItemStack item : inv.getContents()) {
                ItemUtils.giveItemsToPlayer(getPlayer(), item);
            }
            getPlayer().playSound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            isGambling = false;
            getPlayer().closeInventory();
            getPlayer().sendMessage(Component.text("You won!!!!").color(NamedTextColor.GREEN));
        }
    }

    private static final Map<Material, Integer> mats = Map.of(
            Material.DIAMOND, 5,
            Material.EMERALD, 10,
            Material.GOLD_INGOT, 10,
            Material.IRON_INGOT, 10,
            Material.COAL, 16,
            Material.REDSTONE, 32,
            Material.LAPIS_LAZULI, 32,
            Material.NETHERITE_INGOT, 1
    );

    static final Random random = new Random();

    private void setSlotsToItems() {
        List<Material> keys = mats.keySet().stream().toList();
        for (int i = 0; i < inv.getSize(); i++) {
            Material mat = keys.get(random.nextInt(keys.size()));
            ItemStack item = new ItemStack(mat, random.nextInt(1, mats.get(mat) + 1));
            inv.setItem(i, item);
        }
        getPlayer().playSound(getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().equals(getPlayer())) {
            return;
        }
        if (!isGambling) {
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (!isGambling) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
            getPlayer().openInventory(inv);
        }, 1L);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        inv = Bukkit.createInventory(getPlayer(),  InventoryType.HOPPER, Component.text("Gambling!!!").color(NamedTextColor.GREEN) );
        getPlayer().openInventory(inv);
        isGambling = true;
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (inv.getViewers().contains(getPlayer())) {
            getPlayer().closeInventory();
        }
    }
}
