package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpyglassXray extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(SpyglassXray.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get Xray when using a spyglass";
    }

    public SpyglassXray(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerInteractEvent.class);
        subscribeToEvent(PlayerStopUsingItemEvent.class);

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().equals(getPlayer()) || event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.SPYGLASS) {
            return;
        }
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        mobsWithGlowing.clear();
        for (Entity entity : event.getPlayer().getNearbyEntities(50, 50, 50)) {
            entity.setGlowing(true);
            mobsWithGlowing.add(entity.getUniqueId());
        }
    }

    Set<UUID> mobsWithGlowing = new HashSet<>();

    @Override
    public void onPlayerStopUsingItem(PlayerStopUsingItemEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (event.getItem().getType() != Material.SPYGLASS) {
            return;
        }
        for (UUID uuid : mobsWithGlowing) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null) {
                continue;
            }
            entity.setGlowing(false);
        }
        mobsWithGlowing.clear();
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
