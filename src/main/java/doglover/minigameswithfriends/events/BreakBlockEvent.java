package doglover.minigameswithfriends.events;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.gamemodes.RandomizerGamemode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class BreakBlockEvent implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {

        if (!MinigamesWithFriends.getGame().isRunning()) {
            return;
        }
        if (!MinigamesWithFriends.getGame().isGamemodeActive(RandomizerGamemode.class)) {
            return;
        }

        Game game = MinigamesWithFriends.getGame();
        Block block = event.getBlock();

        event.setDropItems((false));
        block.getWorld().dropItemNaturally(block.getLocation(), game.getGamemode(RandomizerGamemode.class).getBlockItem(block.getType()));

    }
}

