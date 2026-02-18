package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Diabetes extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(Diabetes.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You have diabetes";
    }

    @Override
    public void onTick() {
        getGame().sendActionBar("diabetes", getBloodSugarLevelBar(), getPlayer());
    }

    boolean generallyIncreasing = true;

    private void randomlyChangeBloodSugarLevel() {
        double amount = random.nextDouble(11) - 5;
        if (generallyIncreasing) {
            amount += 1;
        } else {
            amount -= 1;
        }
        if (generallyIncreasing && amount > 0) {
            amount *= 2.5;
        } else if (!generallyIncreasing && amount < 0) {
            amount *= 2.5;
        }
        changeBloodSugarLevel(amount);
    }

    private ItemStack getStarterBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("Dr. DogLovePink, PhD");
        meta.setTitle("Intro to Diabetes");
        Component pageOne = Component.text("""
                Congratulations on your diabetes diagnosis! This book will provide you with some basic information about diabetes and how to manage it.
                
                Pages:
                1 Introduction
                2-5 Lowering Blood Sugar
                6-9 Increasing Blood Sugar
                """);
        Component p2 = Component.text("""
                How to Lower Your Blood Sugar Level:
                
                Overview: The main way to lower blood sugar in this world is the Insulin item. Insulin can be crafted at a heated cauldron using common ingredients.
                """);
        Component p3 = Component.text("""
                Step 1 — Prepare the Cauldron:
                
                Fill a cauldron with water until full. To heat it, place a source of heat directly below the cauldron: fire, lava, a campfire, or soul fire work.
                """);
        Component p4 = Component.text("""
                Step 2 — Add Ingredients:
                
                Throw the following into the heated cauldron to craft Insulin:
                 - 1 leather
                 - 1 wheat
                 - 1 gunpowder
                """);
        Component p5 = Component.text("""
                Step 3 — Collect Insulin:
                
                Once the cauldron is heated and the ingredients are present, right-click the cauldron with an empty glass bottle to collect the Insulin.
                """);
        Component p6 = Component.text("""
                How to Increase Your Blood Sugar Level:
                
                Overview: To raise blood sugar you can craft a Glucose Serum. That requires Powdered Glucose as an ingredient.
                """);
        Component p7 = Component.text("""
                Powdered Glucose:
                
                Powdered Glucose is created by cooking sugar on a campfire. Place sugar on a campfire and wait until it cooks to receive powdered glucose.
                """);
        Component p8 = Component.text("""
                Crafting Glucose Serum:
                
                Fill and heat a cauldron as above, then add:
                 - 1 Powdered Glucose
                 - 1 wheat
                 - 1 gunpowder
                Finally, right-click the heated cauldron with an empty glass bottle to collect the Glucose Serum.
                """);
        meta.addPages(pageOne, p2, p3, p4, p5, p6, p7, p8);
        book.setItemMeta(meta);
        return book;
    }

    @Override
    public void on4HertzTick() {
        if (NumberUtils.chanceOf(0.01)) {
            randomlyChangeBloodSugarLevel();
        }
        double bloodSugarLevelForMath = bloodSugarLevel;
        if (bloodSugarLevelForMath >= 86) {
            bloodSugarLevelForMath = 170 - bloodSugarLevel;
        }
        if (bloodSugarLevelForMath <= 50) {
            double chance = (50 - bloodSugarLevelForMath) / (50 * 6);
            if (getPlayer().hasPotionEffect(PotionEffectType.NAUSEA)) {
                chance = chance * 0.65;
            }
            if (NumberUtils.chanceOf(chance)) {
                int duration = (int) Math.round(7 + ((50 - bloodSugarLevelForMath) / 50) * 10) * 20;
                getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, duration, 9));
            }
        }
        if (bloodSugarLevelForMath < 20) {
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 9));
        }
        if (bloodSugarLevelForMath <= 30) {
            int slownessAmount = 0;
            if (bloodSugarLevelForMath <= 20) {
                slownessAmount = 1;
            }
            if (bloodSugarLevelForMath <= 10) {
                slownessAmount = 2;
            }
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, slownessAmount));
        }
        if (bloodSugarLevelForMath <= 30) {
            damageTickCounter++;
            if (damageTickCounter >= 10) {
                damageTickCounter = 0;
                if (!NumberUtils.chanceOf(bloodSugarLevelForMath / 30)) {
                    double damage = 30 / bloodSugarLevelForMath;
                    damage = Math.min(damage, 3);
                    getPlayer().damage(damage);
                }
            }
        }
    }

    int damageTickCounter = 0;

    public Diabetes(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEvent.class);
        subscribeToEvent(BlockCookEvent.class);
        subscribeToEvent(PlayerDeathEvent.class, EventPriority.MONITOR);
        subscribeToEvent(PlayerItemConsumeEvent.class);
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getPlayer().equals(getPlayer()) && !event.isCancelled()) {
            bloodSugarLevel = 85;
            generallyIncreasing = random.nextBoolean();
        }
    }

    private static final NamespacedKey glucosePowderIdentifier = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "is_glucose_powder");

    private ItemStack getPowderedGlucoseItem() {
        ItemStack powderedGlucose = new ItemStack(Material.GLOWSTONE_DUST, 1);

        ItemMeta meta = powderedGlucose.getItemMeta();
        meta.displayName(Component.text("Powdered Glucose").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Ingredient for a blood sugar reducing serum").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        powderedGlucose.setItemMeta(meta);
        powderedGlucose.editPersistentDataContainer(pdc ->
                pdc.set(glucosePowderIdentifier, PersistentDataType.BOOLEAN, true));
        return powderedGlucose;
    }

    private ItemStack getInsulinItem() {
        ItemStack insulin = new ItemStack(Material.POTION, 1);

        PotionMeta meta = (PotionMeta) insulin.getItemMeta();
        meta.setColor(Color.fromRGB(0, 255, 0));
        meta.displayName(Component.text("Insulin").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Decreases your blood sugar").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
        insulin.setItemMeta(meta);
        insulin.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        insulin.editPersistentDataContainer(pdc ->
                pdc.set(key, PersistentDataType.DOUBLE, -40.0));
        return insulin;
    }

    private static final NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "blood_sugar_level_change");

    private ItemStack getGlucoseSerumItem() {
        ItemStack glucoseSerum = new ItemStack(Material.POTION, 1);

        PotionMeta meta = (PotionMeta) glucoseSerum.getItemMeta();
        meta.setColor(Color.fromRGB(255, 0, 0));
        meta.displayName(Component.text("Glucose Serum").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Increases your blood sugar").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        glucoseSerum.setItemMeta(meta);
        glucoseSerum.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        glucoseSerum.editPersistentDataContainer(pdc ->
                pdc.set(key, PersistentDataType.DOUBLE, 40.0));
        return glucoseSerum;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!getPlayer().equals(event.getPlayer())) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
        if (handItem.getType() == Material.SUGAR) {
            handleSugarRefinement(event.getClickedBlock());
        }
        if (handItem.getType() != Material.GLASS_BOTTLE) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        if (!isCauldronValid(clickedBlock)) {
            return;
        }
        handleCrafting(clickedBlock);
        event.setCancelled(true);
    }

    private void handleSugarRefinement(Block clickedBlock) {
        if (clickedBlock == null || (clickedBlock.getType() != Material.CAMPFIRE && clickedBlock.getType() != Material.SOUL_CAMPFIRE)) {
            return;
        }
        Campfire campfire = (Campfire) clickedBlock.getState();
        ItemStack mainHandItem = getPlayer().getInventory().getItemInMainHand();
        for (int i = 0; i < campfire.getSize(); i++) {
            if (campfire.getItem(i) == null) {
                campfire.setItem(i, new ItemStack(Material.SUGAR, 1));
                mainHandItem.setAmount(mainHandItem.getAmount() - 1);
                campfire.setCookTime(i, 1);
                campfire.setCookTimeTotal(i, 160);
                campfire.update(true, true);
                break;
            }
        }
    }

    @Override
    public void onBlockCook(BlockCookEvent event) {
        if (event.getBlock().getType() != Material.CAMPFIRE && event.getBlock().getType() != Material.SOUL_CAMPFIRE) {
            return;
        }
        if (event.getResult().getType().equals(Material.SUGAR)) {
            event.setResult(getPowderedGlucoseItem());
        }
    }

    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        ItemStack item = event.getItem();
        if (item.getPersistentDataContainer().has(key, PersistentDataType.DOUBLE)) {
            Double amount = item.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
            if (amount == null) {
                return;
            }
            changeBloodSugarLevel(amount);
            if (bloodSugarLevel >= 70 && bloodSugarLevel <= 100) {
                generallyIncreasing = random.nextBoolean();
            }
        }
    }

    private void handleCrafting(Block clickedBlock) {
        Item gunPowder = null;
        Item leather = null;
        Item wheat = null;
        Item powderedGlucose = null;

        for (Item item : clickedBlock.getLocation().getNearbyEntitiesByType(Item.class, 0.6)) {
            Material itemStackType = item.getItemStack().getType();
            if (itemStackType.equals(Material.GUNPOWDER)) {
                gunPowder = item;
            } else if (itemStackType.equals(Material.LEATHER)) {
                leather = item;
            } else if (itemStackType.equals(Material.WHEAT)) {
                wheat = item;
            } else if (itemStackType.equals(Material.GLOWSTONE_DUST)) {
                if (item.getItemStack().getPersistentDataContainer().has(glucosePowderIdentifier, PersistentDataType.BOOLEAN)) {
                    powderedGlucose = item;
                }
            }
        }
        ItemStack result;
        if (gunPowder == null || wheat == null) {
            return;
        }
        if (leather != null) {
            leather.getItemStack().setAmount(leather.getItemStack().getAmount() - 1);
            result = getInsulinItem();
        } else if (powderedGlucose != null) {
            powderedGlucose.getItemStack().setAmount(powderedGlucose.getItemStack().getAmount() - 1);
            result = getGlucoseSerumItem();
        } else {
            return;
        }
        gunPowder.getItemStack().setAmount(gunPowder.getItemStack().getAmount() - 1);
        wheat.getItemStack().setAmount(wheat.getItemStack().getAmount() - 1);
        getPlayer().getInventory().getItemInMainHand().setAmount(getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
        getPlayer().give(result);
        getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
    }

    private boolean isCauldronValid(Block block) {
        if (block == null) return false;
        if (block.getType() != Material.WATER_CAULDRON) return false;
        Material belowType = block.getRelative(0, -1, 0).getType();
        return belowType == Material.FIRE
                || belowType == Material.LAVA
                || belowType == Material.SOUL_FIRE
                || belowType == Material.CAMPFIRE
                || belowType == Material.SOUL_CAMPFIRE;
    }

    private void changeBloodSugarLevel(double amount) {
        bloodSugarLevel += amount;
        bloodSugarLevel = Math.max(0.0, Math.min(170.0, bloodSugarLevel));
    }

    private final NamedTextColor[] colors = {
            NamedTextColor.DARK_RED,
            NamedTextColor.GOLD, NamedTextColor.GOLD,
            NamedTextColor.YELLOW, NamedTextColor.YELLOW,
            NamedTextColor.GREEN, NamedTextColor.GREEN, NamedTextColor.GREEN,
            NamedTextColor.DARK_GREEN,
            NamedTextColor.GREEN, NamedTextColor.GREEN, NamedTextColor.GREEN,
            NamedTextColor.YELLOW, NamedTextColor.YELLOW,
            NamedTextColor.GOLD, NamedTextColor.GOLD,
            NamedTextColor.DARK_RED
    };

    //scale of 0 - 170, 50 is best
    private double bloodSugarLevel = 85;

    private Component getBloodSugarLevelBar() {
        double clamped = Math.max(0.0, Math.min(170.0, bloodSugarLevel));
        int markerPosition = (int) Math.round((clamped / 170.0) * 16.0);
        markerPosition = Math.max(0, Math.min(16, markerPosition));
        Component bar = Component.text("-[").color(NamedTextColor.GRAY);

        for (int i = 0; i < 17; i++) {
            if (i == markerPosition) {
                bar = bar.append(Component.text("I").color(NamedTextColor.AQUA));
                continue;
            }
            bar = bar.append(Component.text("|").color(colors[i]));
        }

        bar = bar.append(Component.text("]+").color(NamedTextColor.GRAY));
        return bar;

    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        generallyIncreasing = random.nextBoolean();
        getPlayer().give(getStarterBook());
        getPlayer().sendMessage("<Dr. DogLoverPink> To get you started, I've given you one free treatment on the house!");
        if (generallyIncreasing) {
            getPlayer().give(getInsulinItem());
        } else {
            getPlayer().give(getGlucoseSerumItem());
        }
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
