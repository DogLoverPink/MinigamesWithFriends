package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class DreamBoatClutch extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(DreamBoatClutch.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You have to harness the power of Dream";
    }

    public DreamBoatClutch(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(EntityDamageEvent.class);
    }


    int counter = 0;

    boolean triggered = false;

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || triggered) {
            return;
        }
        triggered = true;
        event.setDamage(0.0);
        Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
            for (ItemStack item : getPlayer().getInventory().getContents()) {
                if (item != null) {
                    getPlayer().dropItem(item);
                }
            }
            getPlayer().getInventory().setContents(inventoryBackup);
            getPlayer().sendMessage("Hehe, just kidding :3");
            Location anvilSpawnLoc = getPlayer().getLocation().clone().add(0, 15, 0);
            getPlayer().getWorld().getBlockAt(anvilSpawnLoc).setType(Material.DAMAGED_ANVIL);
        }, 20);

    }

    @Override
    public void onTick() {
        counter++;
        if (counter == 20) {
            getPlayer().sendMessage("There's a crafting table on the top of it");
        }
        if (counter == 60) {
            getPlayer().sendMessage("All you have to do to save yourself is craft a boat while you're in the air, and place and enter it on the ground");
        }
        if (counter == 100) {
            getPlayer().sendMessage("Sounds easy, right?");
        }
        if (counter == 120) {
            getPlayer().sendMessage("3");
        }
        if (counter == 140) {
            getPlayer().sendMessage("2");
        }
        if (counter == 160) {
            getPlayer().sendMessage("1");
        }
        if (counter == 180) {
            getPlayer().sendMessage("GO!!");
            getPlayer().teleport(teleportLocation);
        }
        if (counter == 320) {
            this.selfDestruct();
        }
    }

    ItemStack[] inventoryBackup;
    Location teleportLocation;

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        inventoryBackup = getPlayer().getInventory().getContents();
        getPlayer().getInventory().clear();
        getPlayer().sendMessage("Okay, you see tower up there?");
        ItemUtils.giveItemsToPlayer(getPlayer(), new ItemStack(Material.OAK_PLANKS, 5));
        Location surfaceBlockLocation = getPlayer().getWorld().getHighestBlockAt(getPlayer().getLocation()).getLocation().add(0.5, 0, 0.5);
        teleportLocation = surfaceBlockLocation.clone().add(0, 60, 0);
        teleportLocation.setDirection(new Vector(90, 0, 0));
        Location offsetLocation = surfaceBlockLocation.clone().add(2, 0, 0);
        World world = getPlayer().getWorld();
        for (double i = offsetLocation.getY(); i < teleportLocation.getY() - 30; i++) {
            Block block = world.getBlockAt(offsetLocation.clone().add(0, i - offsetLocation.getY(), 0));
            if (block.getType().getHardness() > -1) {
                block.setType(Material.OAK_PLANKS);
                if (i == teleportLocation.getY() - 31) {
                    block.setType(Material.CRAFTING_TABLE);
                }
            }
        }
        Location locToTeleport = surfaceBlockLocation.clone().add(0, 1, 0);
        locToTeleport.setDirection(new Vector(90, 0, 0));
        getPlayer().teleport(locToTeleport);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (!triggered) {
            for (ItemStack item : getPlayer().getInventory().getContents()) {
                if (item != null) {
                    getPlayer().dropItem(item);
                }
            }
            getPlayer().getInventory().setContents(inventoryBackup);
        }
    }
}
