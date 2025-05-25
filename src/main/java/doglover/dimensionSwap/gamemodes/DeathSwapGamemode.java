package doglover.dimensionSwap.gamemodes;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeathSwapGamemode extends TimeEventBasedGamemode{

    @Override
    public void tick() {
        super.tick();
        this.getGame().addScoreboardContributution("§dDeath Swap in: §b" + getFormattedTimeRemaining());
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
    }

    @Override
    public void onGameStart() {
        this.setMinTicks(getGame().getConfig().getDeathSwapConfig().getMinimumSecondsBeforeSwap() * 20);
        this.setMaxTicks(getGame().getConfig().getDeathSwapConfig().getMaximumSecondsBeforeSwap() * 20);
        super.onGameStart();
        for (Player plr : this.getGame().getPlayers()) {
            PlayerUtils.resetPlayer(plr);
            plr.sendMessage("§eDeath Swap has begun!");
            this.getGame().addScoreboardContributution("§dDeath Swap in: §b" + (int) (this.getTickGoal() / 20) + " seconds");
        }
    }

    private Map<Player, Player> swapMap = new HashMap<>();

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
        lastTask = Bukkit.getScheduler().runTaskLater(DimensionSwap.getGamePlugin(), this::clearSwappers,600);
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
        lastPlayer.sendMessage(Component.text("eTeleporting to " + plrs.getFirst().getName()));

    }
    @Override
    public String toString() {
        return "DeathSwap";
    }

}
