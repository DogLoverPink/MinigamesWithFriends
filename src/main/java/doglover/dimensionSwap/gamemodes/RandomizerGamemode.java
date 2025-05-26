package doglover.dimensionSwap.gamemodes;

import com.google.common.collect.Lists;
import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.utils.PlayerUtils;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Stream;

public class RandomizerGamemode extends TimeEventBasedGamemode {

    Map<Material, ItemStack> blockMap = new HashMap<>();
    Map<Material, Enchantment> enchantmentMap = new HashMap<>();
    Map<Material, Integer> enchantmentLvlMap = new HashMap<>();
    List<Enchantment> enchantmentList;

    static Random random = new Random();

    public ItemStack getBlockItem(Material material) {
        return blockMap.get(material);
    }

    @Override
    public void onGameEnd() {

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
            if (getGame().getConfig().getRandomizerConfig().isRandomlyEnchantingGear()) {
                DimensionSwap.getGamePlugin().getLogger().info("§aITEM: §b" + itemStack.getType().name() + " §aDURABILITY: §b" + itemStack.getType().getMaxDurability());
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
    public void onGameStart() {
        this.setMinTicks(this.getGame().getConfig().getRandomizerConfig().getMinimumSecondsBeforeDeathMatch() * 20);
        this.setMaxTicks(this.getGame().getConfig().getRandomizerConfig().getMaximumSecondsBeforeDeathMatch() * 20);
        super.onGameStart();
        randomizeBlocks();

    }

    @Override
    public void tick() {
        super.tick();
        if (this.getGame().getConfig().getRandomizerConfig().isEnableDeathMatches()) {
            this.getGame().addScoreboardContributution("§dDeathmatch in: §b" + getFormattedTimeRemaining());
        }
    }

    @Override
    public void onDeathMatchEnd() {
        if (this.getGame().getConfig().getRandomizerConfig().isRerandomizeAfterDeathMatch()) {
            randomizeBlocks();
        }
    }

    @Override
    public void onTimeEventTrigger() {
        if (this.getGame().getConfig().getRandomizerConfig().isEnableDeathMatches()) {
            this.getGame().startDeathMatch();
        }

    }

    @Override
    public String toString() {
        return "Randomizer";
    }
}
