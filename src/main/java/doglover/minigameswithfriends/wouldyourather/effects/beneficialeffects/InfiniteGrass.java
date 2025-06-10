package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InfiniteGrass extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(InfiniteGrass.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get infinite grass";
    }

    public InfiniteGrass(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(BlockPlaceEvent.class);
    }

    static final NamespacedKey key = new NamespacedKey("minigameswithfriends", "infinite_grass");

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.isEmpty() || !item.hasItemMeta()) {
            return;
        }
        if (!item.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN)) {
            return;
        }
        item.setAmount(2);
    }

    private void giveInfiniteGrassItem() {
        ItemStack item = new ItemStack(Material.SHORT_GRASS);
        item.editMeta(meta -> {
            meta.customName(Component.text("Infinite Grass!").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        });
        ItemUtils.giveItemsToPlayer(getPlayer(), item);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        giveInfiniteGrassItem();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
