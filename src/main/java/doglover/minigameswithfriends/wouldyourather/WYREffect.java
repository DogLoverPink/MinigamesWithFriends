package doglover.minigameswithfriends.wouldyourather;

import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class WYREffect {



    public static void test() {

    }


    /**
     * Should a player be able to get this effect twice in the same round
     */
    private boolean isRepeatable = true;

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    private UUID playerUUID;

    public void setPlayer(Player plr) {
        this.playerUUID = plr.getUniqueId();
    }

    public WYREffect(Player player) {
        this.playerUUID = player.getUniqueId();
    }

    public static Random random = new Random();

    public WYREffect() {

    }

    public boolean isPlayerValid() {
        return (getPlayer() != null && getPlayer().isOnline());
    }


    private final List<Class<? extends Event>> eventsToSubscribeTo = new ArrayList<>();
    private final List<EventPriority> eventsToSubscribeToPriority = new ArrayList<>();

    /**
     * Will allow the appropriate event to function, assuming onEffectInitiate is called on this instance
     */
    public void subscribeToEvent(Class<? extends Event> eventClass) {
        subscribeToEvent(eventClass, EventPriority.NORMAL);
    }

    public void subscribeToEvent(Class<? extends Event> eventClass, EventPriority priority) {
        eventsToSubscribeTo.add(eventClass);
        eventsToSubscribeToPriority.add(priority);
    }

    public abstract String getDescriptionBlurb();

    public void onEffectInitiate() {
        for (int i = 0; i < eventsToSubscribeTo.size(); i++) {
            Class<? extends Event> eventClass = eventsToSubscribeTo.get(i);
            EventPriority priority = eventsToSubscribeToPriority.get(i);
            WYREventHandler.subscribe(eventClass, this, priority);
        }
        eventsToSubscribeTo.clear();
        WYREffectHandler.manageEffect(this);
    }

    public void onEffectDecompose() {
        WYREventHandler.unsubscribe(this);
        WYREffectHandler.unmanageEffect(this);
    }

    public void onTick() {

    }

    public void on4HertzTick() {

    }

    public void selfDestruct() {
        WYREffectHandler.decomposeEffectWhenSafe(this);
    }

    private static int effectIdCounter = 0;

    public int getUniqueNumber() {
        return effectIdCounter++;
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

    public Game getGame() {
        return MinigamesWithFriends.getGame();
    }

    public void onBlockCook(BlockCookEvent event) {

    }

    public void onPlayerItemDamage(PlayerItemDamageEvent event) {

    }
}