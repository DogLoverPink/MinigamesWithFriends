package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InfiniteGlisteningMelon extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(InfiniteGlisteningMelon.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You get a lifetime supply of glistering melons";
    }

    public InfiniteGlisteningMelon(Player player) {
        super(player);
        setRepeatable(false);
    }

    @Override
    public void onTick() {
        if (NumberUtils.chanceOf(0.005)) {
            Map<Integer, ItemStack> extras = getPlayer().getInventory().addItem(getMelon());
            for (ItemStack item : extras.values()) {
                getPlayer().getWorld().dropItem(getPlayer().getLocation(), item);
            }
        }
    }

    private ItemStack getMelon() {
        ItemStack melon = new ItemStack(Material.GLISTERING_MELON_SLICE);
        melon.editMeta(meta -> {
            meta.setMaxStackSize(1);
        });
        return melon;
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        for (int i = 0; i < 36; i++) {
            if (getPlayer().getInventory().getItem(i) == null) {
                getPlayer().getInventory().setItem(i, getMelon());
            }
        }
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
