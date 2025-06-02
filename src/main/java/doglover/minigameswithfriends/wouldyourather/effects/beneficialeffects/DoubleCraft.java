package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DoubleCraft extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(DoubleCraft.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Have a chance to double your crafting output";
    }

    public DoubleCraft(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(CraftItemEvent.class);
    }

    @Override
    public void onCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!player.equals(getPlayer())) {
            return;
        }
        List<ItemStack> itemsToCheckForDoubling = new ArrayList<>();
        if (event.getClick() == ClickType.SHIFT_LEFT) {
            for (int i = 0 ; i < getMaxCraftAmount(event.getInventory()); i++) {
                itemsToCheckForDoubling.add(event.getInventory().getResult());
            }
        } else {
            itemsToCheckForDoubling.add(event.getInventory().getResult());
        }
        for (ItemStack item : itemsToCheckForDoubling) {
            if (NumberUtils.chanceOf(0.25) && isAllowedToDouble(item)) {
                ItemUtils.giveItemsToPlayer(getPlayer(), item.clone());
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }
    }

    private static final List<Material> BANNED_DOUBLE_CRAFT_MATERIALS = List.of(
            Material.GOLD_INGOT,
            Material.IRON_INGOT,
            Material.DIAMOND,
            Material.EMERALD,
            Material.NETHERITE_INGOT,
            Material.COAL,
            Material.REDSTONE,
            Material.LAPIS_LAZULI,
            Material.COPPER_INGOT,
            Material.GOLD_BLOCK,
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK,
            Material.NETHERITE_BLOCK,
            Material.COAL_BLOCK,
            Material.REDSTONE_BLOCK,
            Material.LAPIS_BLOCK,
            Material.COPPER_BLOCK,
            Material.IRON_NUGGET,
            Material.GOLD_NUGGET,
            Material.RAW_IRON,
            Material.RAW_GOLD,
            Material.RAW_COPPER,
            Material.RAW_IRON_BLOCK,
            Material.RAW_GOLD_BLOCK,
            Material.RAW_COPPER_BLOCK
    );

    private boolean isAllowedToDouble(ItemStack item) {
        return !BANNED_DOUBLE_CRAFT_MATERIALS.contains(item.getType());
    }

    public int getMaxCraftAmount(CraftingInventory inv) {
        if (inv.getResult() == null)
            return 0;

        int resultCount = inv.getResult().getAmount();
        int materialCount = Integer.MAX_VALUE;

        for (ItemStack is : inv.getMatrix())
            if (is != null && is.getAmount() < materialCount)
                materialCount = is.getAmount();

        return resultCount * materialCount;
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
