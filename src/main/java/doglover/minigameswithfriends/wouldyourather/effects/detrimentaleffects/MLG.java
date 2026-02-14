package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
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

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        ItemStack currentItem = getPlayer().getInventory().getItemInMainHand();
        getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET));
        ItemUtils.giveItemsToPlayer(getPlayer(), currentItem);
        double yLoc = getPlayer().getLocation().getY() + random.nextInt(20, 25);
        World world = getPlayer().getWorld();
        for (double i = getPlayer().getLocation().getY(); i < yLoc + 2; i++) {
            Block block = world.getBlockAt(getPlayer().getLocation().clone().add(0, i- getPlayer().getLocation().getY(), 0));
            if (block.getType().getHardness() > 0) {
                block.setType(Material.AIR);
            }
        }
        Location locToTeleport = getPlayer().getLocation().clone();
        locToTeleport.setY(yLoc);
        locToTeleport.setX(locToTeleport.getBlockX() + 0.5);
        locToTeleport.setZ(locToTeleport.getBlockZ() + 0.5);
        getPlayer().teleport(locToTeleport);
        this.selfDestruct();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
