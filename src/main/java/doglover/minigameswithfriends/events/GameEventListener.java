package doglover.minigameswithfriends.events;

import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;

public class GameEventListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onBlockBreak(event);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onBlockPlace(event);
        }
    }

    @EventHandler
    public void onBlockCook(BlockCookEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onBlockCook(event);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onBlockDamage(event);
        }
    }

    @EventHandler
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onBlockDamageAbort(event);
        }
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onBlockDropItem(event);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityChangeBlock(event);
        }
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerDeath(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityDeath(event);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerRespawn(event);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityDamage(event);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityDamageByEntity(event);
        }
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityDamageByBlock(event);
        }
    }

    @EventHandler
    public void onEntityKnockback(EntityKnockbackEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityKnockback(event);
        }
    }

    @EventHandler
    public void onEntityKnockbackByEntity(EntityKnockbackByEntityEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityKnockbackByEntity(event);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onProjectileHit(event);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!GameEventHandler.isDispatchActive() || event.getHand() == EquipmentSlot.OFF_HAND) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerInteract(event);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!GameEventHandler.isDispatchActive() || event.getHand() == EquipmentSlot.OFF_HAND) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerInteractEntity(event);
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerToggleSneak(event);
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerJump(event);
        }
    }

    @EventHandler
    public void onPlayerInput(PlayerInputEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerInput(event);
        }
    }

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerAttemptPickupItem(event);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onEntityPickupItem(event);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerDropItem(event);
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerItemDamage(event);
        }
    }

    @EventHandler
    public void onPlayerStopUsingItem(PlayerStopUsingItemEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerStopUsingItem(event);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onFoodLevelChange(event);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerItemConsume(event);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onCraftItem(event);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerItemHeld(event);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onInventoryClick(event);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerSwapHandItems(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onInventoryClose(event);
        }
    }

    @EventHandler
    public void onCreeperIgnite(CreeperIgniteEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onCreeperIgnite(event);
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onExplosionPrime(event);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onProjectileLaunch(event);
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (!GameEventHandler.isDispatchActive()) return;
        for (EventSubscriber subscriber : GameEventHandler.getSubscribersForEvent(event)) {
            subscriber.onPlayerFish(event);
        }
    }
}
