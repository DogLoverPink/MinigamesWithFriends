package doglover.minigameswithfriends.events;

import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.LinkedHashMap;
import java.util.Map;


public abstract class EventSubscriber {

    private final Map<Class<? extends Event>, EventPriority> subscriptions = new LinkedHashMap<>();

    /**
     * Informs the event handler that this instance wants to listen to the specified event. This method MUST be called
     * or else none of the overridden event methods will work (this system is in place for performance reasons, event
     * though likely the performance cost would be negligible)
     * @param eventClass The event class to listen to, ex PlayerDeathEvent.class
     */
    public void subscribeToEvent(Class<? extends Event> eventClass) {
        subscribeToEvent(eventClass, EventPriority.NORMAL);
    }

    /**
     * Informs the event handler that this instance wants to listen to the specified event. This method MUST be called
     * or else none of the overridden event methods will work (this system is in place for performance reasons, event
     * though likely the performance cost would be negligible)
     * @param priority The Bukkit event priority of the event (higher priority event runs after lower ones)
     */
    public void subscribeToEvent(Class<? extends Event> eventClass, EventPriority priority) {
        subscriptions.put(eventClass, priority);
    }

    /**
     * Register every declared subscription with the global handler.
     */
    public void registerSubscribedEvents() {
        for (Map.Entry<Class<? extends Event>, EventPriority> entry : subscriptions.entrySet()) {
            GameEventHandler.subscribe(entry.getKey(), this, entry.getValue());
        }
    }

    /**
     * Unsubscribes this instance from listening to events. Make sure to call this if you're not using the class anymore
     */
    public void unregisterSubscribedEvents() {
        GameEventHandler.unsubscribe(this);
    }


    public void onPlayerDeath(PlayerDeathEvent event) {

    }

    public void onEntityDeath(EntityDeathEvent event) {

    }

    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

    }

    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {

    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }

    public void onEntityDamage(EntityDamageEvent event) {

    }

    public void onBlockBreak(BlockBreakEvent event) {

    }

    public void onBlockDropItem(BlockDropItemEvent event) {

    }

    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {

    }

    public void onCraftItem(CraftItemEvent event) {

    }

    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {

    }

    public void onProjectileHit(ProjectileHitEvent event) {

    }

    public void onPlayerDropItem(PlayerDropItemEvent event) {

    }

    public void onInventoryClose(InventoryCloseEvent event) {

    }

    public void onInventoryClick(InventoryClickEvent event) {

    }

    public void onPlayerJump(PlayerJumpEvent event) {

    }

    public void onPlayerItemHeld(PlayerItemHeldEvent event) {

    }

    public void onBlockDamageAbort(BlockDamageAbortEvent event) {

    }

    public void onBlockDamage(BlockDamageEvent event) {

    }

    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {

    }

    public void onBlockPlace(BlockPlaceEvent event) {

    }

    public void onEntityKnockbackByEntity(EntityKnockbackByEntityEvent event) {

    }

    public void onEntityKnockback(EntityKnockbackEvent event) {

    }

    public void onPlayerStopUsingItem(PlayerStopUsingItemEvent event) {

    }

    public void onEntityChangeBlock(EntityChangeBlockEvent event) {

    }

    public void onFoodLevelChange(FoodLevelChangeEvent event) {

    }

    public void onCreeperIgnite(CreeperIgniteEvent event) {

    }

    public void onExplosionPrime(ExplosionPrimeEvent event) {

    }

    public void onProjectileLaunch(ProjectileLaunchEvent event) {

    }

    public void onPlayerInput(PlayerInputEvent event) {

    }

    public void onPlayerFish(PlayerFishEvent event) {

    }

    public void onBlockCook(BlockCookEvent event) {

    }

    public void onPlayerItemDamage(PlayerItemDamageEvent event) {

    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {

    }

    public void onEntityPickupItem(EntityPickupItemEvent event) {

    }

    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {

    }
}
