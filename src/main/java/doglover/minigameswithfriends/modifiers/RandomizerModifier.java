package doglover.minigameswithfriends.modifiers;

import com.google.common.collect.Lists;
import doglover.minigameswithfriends.configs.RandomizerConfig;
import doglover.minigameswithfriends.gamemodes.Modifier;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Stream;

public class RandomizerModifier extends Modifier {

    private static final RandomizerConfig CONFIG = new RandomizerConfig();

    static {
        Modifier.register("Randomizer", RandomizerModifier.class, RandomizerModifier::new, CONFIG);
    }

    @Override
    public RandomizerConfig getConfig() {
        return CONFIG;
    }

    Map<Material, ItemStack> blockMap = new HashMap<>();
    Map<Material, Enchantment> enchantmentMap = new HashMap<>();
    Map<Material, Integer> enchantmentLvlMap = new HashMap<>();
    List<Enchantment> enchantmentList;

    static Random random = new Random();

    public RandomizerModifier() {
        subscribeToEvent(BlockBreakEvent.class);
    }

    public ItemStack getBlockItem(Material material) {
        return blockMap.get(material);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), getBlockItem(block.getType()));
    }

    public void randomizeBlocks() {
        enchantmentList = Lists.newArrayList(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).iterator());
        enchantmentMap.clear();
        enchantmentLvlMap.clear();
        List<Material> materials = new ArrayList<>(List.of(Material.values()));
        materials = materials.stream().filter(Material::isBlock).toList();
        List<Material> materials2 = new ArrayList<>(Stream.of(Material.values()).filter(Material::isItem).toList());
        Collections.shuffle(materials2);

        blockMap.clear();
        for (int i = 0; i < materials.size(); i++) {
            ItemStack itemStack = new ItemStack(materials2.get(i));
            if (getConfig().isRandomlyEnchantingGear()) {
                randomlyEnchantItemStack(itemStack);
            }
            blockMap.put(materials.get(i), itemStack);
        }
    }

    public void randomlyEnchantItemStack(ItemStack itemStack) {
        if (itemStack.getType().isItem() && itemStack.getType().getMaxDurability() > 1) {
            if (!enchantmentMap.containsKey(itemStack.getType())) {
                int level = random.nextInt(1, 6); // Random enchantment level between 1 and 5
                Enchantment enchantment = enchantmentList.get(random.nextInt(enchantmentList.size()));
                enchantmentMap.put(itemStack.getType(), enchantment);
                enchantmentLvlMap.put(itemStack.getType(), level);
            }
            itemStack.addUnsafeEnchantment(enchantmentMap.get(itemStack.getType()), enchantmentLvlMap.get(itemStack.getType()));
        }

    }

    @Override
    public void tick() {

    }

    @Override
    public void onGameStart() {
        randomizeBlocks();
    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void updateConfig() {

    }

    @Override
    public void onDeathMatchEnd() {
        if (getConfig().isRerandomizeAfterDeathMatch()) {
            randomizeBlocks();
        }
    }

    @Override
    public String toString() {
        return "Randomizer";
    }
}
