package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SmeltingTouch extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(SmeltingTouch.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Smelt ores instantly when mining them";
    }

    public SmeltingTouch(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(BlockDropItemEvent.class);
    }

    @Override
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        List<Item> drops = event.getItems();
        for (Item dropEntity : drops) {
            ItemStack drop = dropEntity.getItemStack();
            ItemStack smeltResult = getSmeltResult(drop);
            if (smeltResult != null && allowedSmeltingMaterials.contains(smeltResult.getType())) {
                dropEntity.setItemStack(smeltResult);
            }
        }
    }

    private static final List<Material> allowedSmeltingMaterials = List.of(
            Material.IRON_INGOT, Material.GOLD_INGOT, Material.COPPER_INGOT
    );

    private static ItemStack getSmeltResult(ItemStack input) {
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe recipe = recipes.next();
            if (recipe instanceof FurnaceRecipe furnace) {
                if (furnace.getInput().getType() == input.getType()) {
                    return new ItemStack(furnace.getResult().getType(), input.getAmount());
                }
            }
        }
        return null;
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
