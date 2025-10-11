package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class FullDiamond extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(FullDiamond.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get a full set of diamond tools";
    }

    public FullDiamond(Player player) {
        super(player);
        setRepeatable(true);
    }

    @Override
    public void onEffectInitiate() {
        Material[] mats = new Material[]{
                Material.DIAMOND_SWORD,
                Material.DIAMOND_PICKAXE,
                Material.DIAMOND_AXE,
                Material.DIAMOND_SHOVEL,
                Material.DIAMOND_HOE,
                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS
        };
        for (Material mat : mats) {
            ItemStack item = new ItemStack(mat);
            Damageable damageable = (Damageable) item.getItemMeta();
            damageable.setDamage(item.getType().getMaxDurability());
            item.setItemMeta(damageable);
            ItemUtils.giveItemsToPlayer(getPlayer(), item);
        }
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
