package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import doglover.minigameswithfriends.utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Phoenix extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(Phoenix.class);
    }

    private boolean isInPhoenixMode = false;
    private ItemStack[] inventoryBackup = new ItemStack[36];
    private Location lastLocation = null;

    @Override
    public String getDescriptionBlurb() {
        return "Harness the power of the phoenix";
    }

    public Phoenix(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerInteractEvent.class);
        subscribeToEvent(PlayerDeathEvent.class);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != getPlayer()) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item.getType() == Material.MAGMA_CREAM && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("minigameswithfriends", "phoenix_essence"))) {
            getPlayer().getInventory().remove(item);
            getPlayer().sendMessage(TextUtils.MINI_MESSAGE.deserialize("<gold><bold>You feel the warmth of the phoenix's fire overtake you..."));
            activatePhoenixMode();
        }
    }

    public ItemStack getFireItem(int amount) {
        ItemStack fire = new ItemStack(Material.BLAZE_POWDER);
        fire.editMeta(meta -> {
            meta.customName(TextUtils.MINI_MESSAGE.deserialize("<gold><bold>Phoenix's Fire"));
            meta.lore(Arrays.asList(
                    TextUtils.MINI_MESSAGE.deserialize("<red>The charred remains of what was once your hand..."),
                    TextUtils.MINI_MESSAGE.deserialize("<gray>Property of "+getPlayer().getName()).decoration(TextDecoration.ITALIC, false)
            ));
        });
        fire.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        fire.addUnsafeEnchantment(Enchantment.SHARPNESS, 2);
        fire.setAmount(amount);
        return fire;
    }

    public void fillInventoryWithFire(int amount) {
        for (int i = 0; i < 36; i++) {

            getPlayer().getInventory().setItem(i, getFireItem(amount));

        }
    }

    private BukkitTask task;

    public void activatePhoenixMode() {


        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60 * 20, 0, false, false));
        getPlayer().setHealth(getPlayer().getAttribute(Attribute.MAX_HEALTH).getValue() / 2.0);
        isInPhoenixMode = true;
        inventoryBackup = getPlayer().getInventory().getContents().clone();
        getPlayer().getInventory().clear();
        BukkitRunnable run = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i < 5) {

                    fillInventoryWithFire(5 - i);
                } else {
                    getPlayer().getInventory().clear();
                    getPlayer().getInventory().setContents(inventoryBackup);
                    isInPhoenixMode = false;
                    cancel();

                    this.cancel();
                }
                i++;
            }
        };
        task = run.runTaskTimer(MinigamesWithFriends.getGamePlugin(), 0, 20);

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }

        for (ItemStack item : getPlayer().getInventory().getContents()) {
            if (item != null && item.getType() == Material.MAGMA_CREAM && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("minigameswithfriends", "phoenix_essence"))) {
                getPlayer().getInventory().remove(item);
                event.setCancelled(true);
                lastLocation = getPlayer().getLocation().clone();
//                getPlayer().spigot().respawn();
                getPlayer().sendMessage(TextUtils.MINI_MESSAGE.deserialize("<gold><bold>Your ashes rise from the dead!"));
                activatePhoenixMode();
                Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
                    getPlayer().teleport(lastLocation);
                }, 1L);
                break;
            }
        }


    }

    @Override
    public void onTick() {
        if (isInPhoenixMode) {
            sphereAround(getPlayer().getLocation(), 4);
            getPlayer().setFireTicks(0);
        }
    }

    public static Set<Block> sphereAround(Location location, int radius) {
        Set<Block> sphere = new HashSet<>();
        Block center = location.getBlock();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block b = center.getRelative(x, y + 1, z);
                    if (center.getLocation().distanceSquared(b.getLocation()) <= radius * radius) {
                        if (b.getType().isAir()) {
                            b.setType(Material.FIRE);
                        }

                    }
                }
            }

        }
        return sphere;
    }

    @Override
    public void onEffectInitiate() {

        super.onEffectInitiate();

        ItemStack item = new ItemStack(Material.MAGMA_CREAM);
        List<Component> lore = Arrays.asList(TextUtils.MINI_MESSAGE.deserialize("<yellow>Right click to use (One time use)"), TextUtils.MINI_MESSAGE.deserialize("<red>Will trigger automatically on death, bringing you back to life"));
        item.editMeta(meta -> {
            meta.customName(TextUtils.MINI_MESSAGE.deserialize("<dark_red><bold>Phoenix's Essence"));
            meta.lore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey("minigameswithfriends", "phoenix_essence"), PersistentDataType.STRING, "phoenix_essence");
        });
        getPlayer().getInventory().addItem(item);


    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }
}
