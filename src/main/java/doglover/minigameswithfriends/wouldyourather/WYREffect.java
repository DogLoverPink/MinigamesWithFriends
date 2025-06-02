package doglover.minigameswithfriends.wouldyourather;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class WYREffect {


    protected Player player;

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
        return player;
    }

    public void setPlayer(Player plr) {
        this.player = plr;
    }

    public WYREffect(Player player) {
        this.player = player;
    }

    public WYREffect() {

    }

    private final List<Class<? extends Event>> eventsToSubscribeTo = new ArrayList<>();

    /**
     * Will allow the appropriate event to function, assuming onEffectInitiate is called on this instance
     */
    public void subscribeToEvent(Class<? extends Event> eventClass) {
        eventsToSubscribeTo.add(eventClass);
    }

    public abstract String getDescriptionBlurb();

    public void onEffectInitiate() {
        for (Class<? extends Event> eventClass : eventsToSubscribeTo) {
            WYREventHandler.subscribe(eventClass, this);
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

    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {

    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }

    public void onEntityDamage(EntityDamageEvent event) {

    }

    public void onBlockBreak(BlockBreakEvent event) {

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
}
