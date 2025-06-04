package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.util.TriState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MLG extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(MLG.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You have to do an MLG";
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
        double yLoc = getPlayer().getLocation().getY() + random.nextInt(20, 25);
        World world = getPlayer().getWorld();
        for (double i = getPlayer().getLocation().getY(); i < yLoc; i++) {
            Block block = world.getBlockAt(getPlayer().getLocation().clone().add(0, i, 0));
            if (block.getType().getHardness() > 0) {
                block.setType(Material.AIR);
            }
        }
        Location locToTeleport = getPlayer().getLocation().clone();
        locToTeleport.setY(yLoc);
        getPlayer().teleport(locToTeleport);
        this.selfDestruct();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
