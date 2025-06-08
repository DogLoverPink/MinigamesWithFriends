package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class InventoryRelocation extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(InventoryRelocation.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Your inventory gets misplaced in shipping";
    }

    public InventoryRelocation(Player player) {
        super(player);
        setRepeatable(true);
    }

    static final Random random = new Random();

    private Location getRandomizedLocation() {
        double offsetX = getPlayer().getLocation().getX() + random.nextInt(24) - 12;
        double offsetY = getPlayer().getLocation().getY() + random.nextInt(24) - 12;
        double offsetZ = getPlayer().getLocation().getZ() + random.nextInt(24) - 12;
        return new Location(player.getWorld(), offsetX, offsetY, offsetZ);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        Location newLoc = getRandomizedLocation();
        String posString = newLoc.getBlockX() + ", "+ newLoc.getBlockY() + ", "+ newLoc.getBlockZ();
        Component message = Component.text("<UPS_Guy> Sorry, we mighta misplaced your stuff a bit, I think I might have put it around "+posString);
        getPlayer().sendMessage(message);
        getPlayer().getWorld().getBlockAt(newLoc).setType(Material.CHEST);
        Block block = getPlayer().getWorld().getBlockAt(newLoc);
        Chest chest = (Chest) block.getState();
        for (int i = 9; i < 36; i++) {
            ItemStack item = getPlayer().getInventory().getItem(i);
            if (item == null) {
                continue;
            }
            chest.getInventory().setItem(i - 9, item.clone());
            getPlayer().getInventory().remove(item);
        }
        this.selfDestruct();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
