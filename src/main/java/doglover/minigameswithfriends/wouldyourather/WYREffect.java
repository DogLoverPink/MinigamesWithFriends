package doglover.minigameswithfriends.wouldyourather;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.events.EventSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public abstract class WYREffect extends EventSubscriber {

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

    public abstract String getDescriptionBlurb();

    public void onEffectInitiate() {
        registerSubscribedEvents();
        WYREffectHandler.manageEffect(this);
    }

    public void onEffectDecompose() {
        unregisterSubscribedEvents();
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

    public Game getGame() {
        return MinigamesWithFriends.getGame();
    }
}
