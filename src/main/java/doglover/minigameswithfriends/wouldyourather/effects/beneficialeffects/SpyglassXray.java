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
        setRepeatable(false);

    }


    Set<UUID> mobsWithGlowing = new HashSet<>();

    @Override
    public void onTick() {
        if (getPlayer().hasActiveItem() && getPlayer().getActiveItem().getType() == Material.SPYGLASS) {
            if (getPlayer().getActiveItemUsedTime() == 3) {
                mobsWithGlowing.clear();
                for (Entity entity : getPlayer().getNearbyEntities(50, 50, 50)) {
                    entity.setGlowing(true);
                    mobsWithGlowing.add(entity.getUniqueId());
                }
            }
        } else {
            stopGlowing();
        }
    }


    private void stopGlowing() {
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
        getPlayer().sendMessage("Yes yes, I am aware, but I'm too lazy to fix it");
        getPlayer().sendMessage(" - DogLoverPink");
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        stopGlowing();
    }
}
