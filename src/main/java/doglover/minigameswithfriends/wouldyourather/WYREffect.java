package doglover.minigameswithfriends.wouldyourather;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class WYREffect {


    protected Player player;

    public static void test() {

    }


    /**Should a player be able to get this effect twice in the same round */
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

    /**Will allow the appropriate event to function, assuming onEffectInitiate is called on this instance*/
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

}
