package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.configs.DeathSwapConfig;
import doglover.minigameswithfriends.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeathSwapGamemode extends TimeEventBasedGamemode{

    public DeathSwapGamemode() {
        subscribeToEvent(PlayerDeathEvent.class, EventPriority.HIGH);
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.broadcast(Component.text("High priority event"));
        if (getGame().isInDeathMatch()) {
            return;
        }
        DeathSwapConfig config = getGame().getConfig().getDeathSwapConfig();
        Player swapper = getSwapperFromSwapee(event.getPlayer());
        if (config.shouldKeepInventoryOnSwapRelatedDeath() && swapper != null) {
            event.setKeepInventory(true);
        }
        if (swapper == null) {
            return;
        }
        int pointsToGive = config.getPointsPerImpressiveDeath();
        DamageType damageType = event.getDamageSource().getDamageType();
        if (damageType == DamageType.FALL
                || damageType == DamageType.LAVA
                || damageType == DamageType.OUT_OF_WORLD) {
            pointsToGive = config.getPointsPerLameDeath();
        }
        getGame().addPointsToPlayer(swapper, pointsToGive);
        nullifySwapper(event.getPlayer());
    }

    @Override
    public void tick() {
        super.tick();
        this.getGame().addScoreboardContribution("§dDeath Swap in: §b" + getFormattedTimeRemaining());
        if (getTickGoal() == 200) {
            for (Player player : getGame().getPlayers()) {
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 2f);
            }
        }
        if (getTickGoal() <= 60 && getTickGoal() % 20 == 0) {
            for (Player player : getGame().getPlayers()) {
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
            }
        }
    }

    @Override
    public void onGameEnd() {
    }

    @Override
    public void onGameStart() {
        updateConfig();
        super.onGameStart();
        for (Player plr : this.getGame().getPlayers()) {
            PlayerUtils.resetPlayer(plr);
            plr.sendMessage("§eDeath Swap has begun!");
            this.getGame().addScoreboardContribution("§dDeath Swap in: §b" + (int) (this.getTickGoal() / 20) + " seconds");
        }
    }

    @Override
    public void updateConfig() {
        this.setMinTicks(getGame().getConfig().getDeathSwapConfig().getMinimumSecondsBeforeSwap() * 20);
        this.setMaxTicks(getGame().getConfig().getDeathSwapConfig().getMaximumSecondsBeforeSwap() * 20);
    }

    private final Map<Player, Player> swapMap = new HashMap<>();

    /**
     * Gives the player who most recently swapped with the swapee.
     * @param swapee The player who you want to get the swapper for.
     * @return The player who swapped with them most recently.
     */
    public Player getSwapperFromSwapee(Player swapee) {
        return swapMap.get(swapee);
    }
    public void nullifySwapper(Player swapee) {
        swapMap.put(swapee, null);
        swapMap.remove(swapee);
    }

    public void clearSwappers() {
        swapMap.clear();
    }

    BukkitTask lastTask;


    @Override
    public void onTimeEventTrigger() {
        if (lastTask != null) {
            lastTask.cancel();
        }
        lastTask = Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), this::clearSwappers,600);
        ArrayList<Player> plrs = new ArrayList<>(this.getGame().getPlayers());
        Collections.shuffle(plrs);
        Location loc1 = plrs.getFirst().getLocation();
        for (int i = 0; i < plrs.size() - 1; i++) {
            Player plr = plrs.get(i);
            swapMap.put(plr, plrs.get(i + 1));
            plr.teleport(plrs.get(i + 1).getLocation());
            plr.sendMessage(Component.text("§eTeleporting to " + plrs.get(i + 1).getName()));
        }
        Player lastPlayer = plrs.getLast();
        swapMap.put(lastPlayer, plrs.getFirst());
        lastPlayer.teleport(loc1);
        lastPlayer.sendMessage(Component.text("§eTeleporting to " + plrs.getFirst().getName()));

    }
    @Override
    public String toString() {
        return "DeathSwap";
    }

}
