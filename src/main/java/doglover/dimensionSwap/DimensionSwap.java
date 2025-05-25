package doglover.dimensionSwap;

import doglover.dimensionSwap.commands.DimensionSwapCommand;
import doglover.dimensionSwap.commands.GameCommandTabCompleter;
import doglover.dimensionSwap.events.BreakBlockEvent;
import doglover.dimensionSwap.events.CommandBlockRunEvent;
import doglover.dimensionSwap.events.DeathListener;
import doglover.dimensionSwap.gamemodes.DimensionSwapGamemode;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class DimensionSwap extends JavaPlugin {

    public static Plugin getGamePlugin() {
        return DimensionSwap.getPlugin(DimensionSwap.class);
    }

    static Game game;

    public static Game getGame() {
        return game;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Plugin is starting up!");
        PluginCommand minigameCommand = this.getCommand("minigames");
        minigameCommand.setExecutor(new DimensionSwapCommand());
        minigameCommand.setTabCompleter(new GameCommandTabCompleter());
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new CommandBlockRunEvent(), this);
        DimensionSwapGamemode.initialize();
        Game swapGame = new Game();
        game = swapGame;
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (game.isRunning()) {
                game.tick();
            }
        }, 0, 1);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
