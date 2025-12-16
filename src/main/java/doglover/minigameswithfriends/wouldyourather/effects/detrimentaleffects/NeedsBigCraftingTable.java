package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class NeedsBigCraftingTable extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(NeedsBigCraftingTable.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You can only use a crafting table if it's a part of a 2x2 square of placed crafting tables";
    }

    public NeedsBigCraftingTable(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    private boolean isValidCraftingTable(Location originalTableLocation) {
        int tableCount = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location checkLocation = originalTableLocation.clone().add(x, 0, z);
                if (checkLocation.getBlock().getType().equals(Material.CRAFTING_TABLE)) {
                    tableCount++;
                }
            }
        }
        return tableCount >= 4;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (getPlayer().isSneaking() && getPlayer().getInventory().getItemInMainHand().getType().isBlock()) {
            return;
        }

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CRAFTING_TABLE) {
            Location clickedLocation = event.getClickedBlock().getLocation();
            if (!isValidCraftingTable(clickedLocation)) {
                event.setCancelled(true);
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1.0f, 0.5f);
            }
        }
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
