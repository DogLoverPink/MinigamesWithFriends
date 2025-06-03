package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MLG extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(MLG.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You'd better be good at MLGs";
    }

    public MLG(Player player) {
        super(player);
        setRepeatable(true);
    }

    static final Random random = new Random();

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        ItemUtils.giveItemsToPlayer(getPlayer(), getPlayer().getInventory().getItemInMainHand());
        getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET));
        getPlayer().teleport(getPlayer().getLocation().add(0 , random.nextInt(20, 25), 0));
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
