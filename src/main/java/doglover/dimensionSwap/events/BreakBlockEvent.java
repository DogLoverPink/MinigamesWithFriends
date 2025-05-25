package doglover.dimensionSwap.events;

import doglover.dimensionSwap.DimensionSwap;
import doglover.dimensionSwap.Game;
import doglover.dimensionSwap.gamemodes.RandomizerGamemode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


public class BreakBlockEvent implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {

        if (!DimensionSwap.getGame().isRunning()) {
            return;
        }
        if (!DimensionSwap.getGame().isGamemodeActive(RandomizerGamemode.class)) {
            return;
        }

        Game game = DimensionSwap.getGame();
        Block block = event.getBlock();

        event.setDropItems((false));
        block.getWorld().dropItemNaturally(block.getLocation(), game.getGamemode(RandomizerGamemode.class).getBlockItem(block.getType()));

    }
}

