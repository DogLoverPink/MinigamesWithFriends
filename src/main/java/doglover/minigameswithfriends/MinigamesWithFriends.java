package doglover.minigameswithfriends;

import doglover.minigameswithfriends.commands.BuiltInCommandDefinitions;
import doglover.minigameswithfriends.commands.CommandHandler;
import doglover.minigameswithfriends.commands.MinigamesWithFriendCommand;
import doglover.minigameswithfriends.commands.GameCommandTabCompleter;
import doglover.minigameswithfriends.events.*;
import doglover.minigameswithfriends.gamemodes.DimensionSwapGamemode;
import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.utils.JarUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinigamesWithFriends extends JavaPlugin {

    public static Plugin getGamePlugin() {
        return MinigamesWithFriends.getPlugin(MinigamesWithFriends.class);
    }

    static Game game;

    public static Game getGame() {
        return game;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Plugin is starting up!");
        PluginCommand minigameCommand = this.getCommand("minigames");
        BuiltInCommandDefinitions.registerCommands();

        //Run all static blocks, to make sure any registered commands are ran on startup (this isn't an ai slop comment I swear)
        JarUtils.initalizeClassesInPackage("doglover.minigameswithfriends.gamemodes");
        JarUtils.initalizeClassesInPackage("doglover.minigameswithfriends.modifiers");

        minigameCommand.setExecutor(new MinigamesWithFriendCommand());
        minigameCommand.setTabCompleter(new GameCommandTabCompleter());
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new CommandBlockRunEvent(), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        DimensionSwapGamemode.initialize();
        WouldYouRatherGamemode.initialize();

        GameEventHandler.registerListeners(this);

        game = new Game();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (game.isRunning()) {
                game.tick();
            }
        }, 0, 1);


    }

    @Override
    public void onDisable() {
        getGame().endGame();
    }
}
