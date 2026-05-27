package doglover.minigameswithfriends.commands;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.configs.GamemodeConfig;
import doglover.minigameswithfriends.gamemodes.BlockShuffleGamemode;
import doglover.minigameswithfriends.gamemodes.DimensionSwapGamemode;
import doglover.minigameswithfriends.gamemodes.Gamemode;
import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.PlayerUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import doglover.minigameswithfriends.wouldyourather.WYREventHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class MinigamesWithFriendCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return true;
        }

        String minigameCommand = args[0];

        if (minigameCommand.equalsIgnoreCase("EnableGamemode")) {
            handleEnableGamemode(commandSender, args);
        } else if (minigameCommand.equalsIgnoreCase("SafeLoc")) {
            Player plr = (Player) commandSender;
            plr.setFallDistance(0);
            plr.teleport(BlockUtils.findSafeBlock(plr.getLocation()));
        } else if (minigameCommand.equalsIgnoreCase("dimensionswap")) {
            handleDimensionSwapCommand(commandSender, args);
        } else if (minigameCommand.equalsIgnoreCase("help")) {
            handleHelpCommand(commandSender, args);
        } else if (minigameCommand.equalsIgnoreCase("launchToSpawn")) {
            Player plr = (Player) commandSender;
            PlayerUtils.launchPlayerToLoc(plr, plr.getWorld().getSpawnLocation());
        } else if (minigameCommand.equalsIgnoreCase("DisableGamemode")) {
            handleDisableGamemode(commandSender, args);
        } else if (minigameCommand.equalsIgnoreCase("ClearGamemodes")) {
            MinigamesWithFriends.getGame().clearGamemodes();
            commandSender.sendMessage("§aCleared all gamemodes.");
        } else if (minigameCommand.equalsIgnoreCase("start")) {
            if (MinigamesWithFriends.getGame().getGamemodes().isEmpty()) {
                commandSender.sendMessage("§cNo gamemodes enabled. Add some by doing §e/minigames EnableGamemode <gamemodeName>.");
                return true;
            }
            MinigamesWithFriends.getGame().startGame();
            commandSender.sendMessage("§aGame started.");
        } else if (minigameCommand.equalsIgnoreCase("stop")) {
            MinigamesWithFriends.getGame().endGame();
            commandSender.sendMessage("§cGame stopped.");
        } else if (minigameCommand.equalsIgnoreCase("blockshuffle")) {
            handleBlockShuffleCommand(commandSender, args);
        } else if (minigameCommand.equalsIgnoreCase("Pause")) {
            handlePause(commandSender);
        } else if (minigameCommand.equalsIgnoreCase("Unpause")) {
            handleUnpause(commandSender);
        } else if (minigameCommand.equalsIgnoreCase("wouldyourather")) {
            handleWouldYouRatherCommand(commandSender, args);
        } else if (minigameCommand.equalsIgnoreCase("config")) {
            if (args.length == 1) {
                commandSender.sendMessage("§cSpecify a gamemode and a config key.");
                commandSender.sendMessage("§eEx.: /dimensionSwap config dimensionSwap canVisitSameWorldTwice true");
                return true;
            }
            if (args.length == 2) {
                String key = args[1];
                handleConfig2Args(commandSender, key);
                return true;
            }
            if (args.length == 3) {
                handleFetchingConfigValue(commandSender, args[1], args[2]);
                return true;
            }
            if (args.length == 4) {
                handleSettingConfigValue(commandSender, args[1], args[2], args[3]);
            }
        }
        else if (minigameCommand.equalsIgnoreCase("addspectator")) {
            addSpectator(commandSender, args);

        } else if (minigameCommand.equalsIgnoreCase("removespectator")) {
            removeSpectator(commandSender, args);
        }
        else {
            commandSender.sendMessage(Component.text("Invalid command!").color(NamedTextColor.RED));
        }
        return false;
    }

    private void removeSpectator(CommandSender sender, String[] args) {
        if  (args.length == 1) {
            sender.sendMessage("§cPlease specify a player!");
            return;
        }
        Player plr = Bukkit.getPlayer(args[1]);
        if (plr == null) {
            sender.sendMessage("§cPlease specify a valid player!");
            return;
        }
        if (!MinigamesWithFriends.getGame().getSpectators().contains(plr)) {
            sender.sendMessage("§cThat player is not a spectator!");
            return;
        }
        MinigamesWithFriends.getGame().removeSpectator(plr);
        sender.sendMessage("§aMade §b"+plr.getName()+"§a no longer a spectator!");
    }

    private void addSpectator(CommandSender sender, String[] args) {
        if  (args.length == 1) {
            sender.sendMessage("§cPlease specify a player!");
            return;
        }
        Player plr = Bukkit.getPlayer(args[1]);
        if (plr == null) {
            sender.sendMessage("§cPlease specify a valid player!");
            return;
        }
        if (MinigamesWithFriends.getGame().getSpectators().contains(plr)) {
            sender.sendMessage("§cThat player is already a spectator!");
            return;
        }
        MinigamesWithFriends.getGame().addSpectator(plr);
        if (!sender.equals(plr)) {
            sender.sendMessage("§aMade §b"+plr.getName()+"§a a spectator!");
        }
        if (!MinigamesWithFriends.getGame().isRunning()) {
            plr.sendMessage("§aYou have been set to spectate the next game!");
        }
    }

    private void handlePause(CommandSender commandSender) {
        Game game = MinigamesWithFriends.getGame();
        if (game.isPaused()) {
            commandSender.sendMessage(Component.text("Already paused!").color(NamedTextColor.RED));
            return;
        }
        game.setPaused(true);
        if (game.isGamemodeActive(WouldYouRatherGamemode.class)) {
            WYREventHandler.setActive(false);
        }
        game.getPlayers().forEach(player -> {
            player.sendMessage(Component.text("Game Paused!").color(NamedTextColor.GREEN));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);
        });
        commandSender.sendMessage(Component.text("Timers have been paused, but note that pausing is not fully supported by every part of this plugin").color(NamedTextColor.YELLOW));
    }

    private void handleUnpause(CommandSender commandSender) {
        Game game = MinigamesWithFriends.getGame();
        if (!game.isPaused()) {
            commandSender.sendMessage(Component.text("Game not currently paused!").color(NamedTextColor.RED));
            return;
        }
        game.setPaused(false);
        if (game.isGamemodeActive(WouldYouRatherGamemode.class)) {
            WYREventHandler.setActive(true);
        }
        game.getPlayers().forEach(player -> {
            player.sendMessage(Component.text("Game Unpaused!").color(NamedTextColor.GREEN));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);
        });
    }

    private void handleHelpCommand(CommandSender commandSender, String[] args) {
        Component message = MiniMessage.miniMessage().deserialize(
                "<yellow>For help with the plugin, refer to <click:open_url:'https://REPLACEMEWITHDOCSLINK.com'><u><aqua>the documentation</aqua></u></click></yellow>"
        );
        message = message.hoverEvent(Component.text("Click to open the documentation.").color(NamedTextColor.AQUA));
        commandSender.sendMessage(message);
    }

    private void handleDimensionSwapCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("PreLoadSavedDimensionSwapWorlds")) {
            DimensionSwapGamemode.preLoadSavedWorlds(commandSender);
        } else {
            commandSender.sendMessage("§cInvalid subcommand!");
        }
    }

    private void handleWouldYouRatherCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("RemoveEffect")) {
            if (args.length < 4) {
                commandSender.sendMessage("§cInvalid usage! Do §e/mg wouldyourather removeEffect <player> <effect>");
                return;
            }
            Player plr = Bukkit.getPlayer(args[2]);
            if (plr == null) {
                commandSender.sendMessage("§cUnknown player §e" + args[2]);
                return;
            }
            String effectToRemove = args[3];
            Iterator<WYREffect> effectIterator = WYREffectHandler.getManagedEffects().iterator();
            while (effectIterator.hasNext()) {
                WYREffect effect = effectIterator.next();
                if (effect.getPlayer().equals(plr) && effect.getClass().getSimpleName().equalsIgnoreCase(effectToRemove)) {
                    effect.selfDestruct();
                    commandSender.sendMessage("§aRemoved §e" + effectToRemove + "§a from §e" + plr.getName());
                    effectIterator.remove();
                    return;
                }
            }
            commandSender.sendMessage("§cEffect not found!");
        }
        if (args[1].equalsIgnoreCase("CheckActiveEffects")) {
            Player plr;
            if (args.length < 3 && commandSender instanceof Player) {
                plr = (Player) commandSender;
            } else if (args.length >= 3) {
                plr = Bukkit.getPlayer(args[2]);
                if (plr == null) {
                    commandSender.sendMessage("§cUnknown player §e" + args[2]);
                    return;
                }
            } else {
                commandSender.sendMessage("§cUnknown player");
                return;
            }
            if (!WYREventHandler.isActive() || WYREffectHandler.getManagedEffects().isEmpty()) {
                commandSender.sendMessage("§e" + plr.getName() + "§c has no active effects.");
                return;
            }
            commandSender.sendMessage("§eActive effects on §b" + plr.getName() + "§e:");
            for (WYREffect effect : WYREffectHandler.getManagedEffects()) {
                if (effect.getPlayer().equals(plr)) {
                    NamedTextColor color = WYREffectHandler.isEffectBeneficial(effect) ? NamedTextColor.GREEN : NamedTextColor.RED;
                    TextComponent effectComponent = Component.text(" - ").color(NamedTextColor.YELLOW);
                    effectComponent = effectComponent.append(Component.text(effect.getClass().getSimpleName()).color(color));
                    effectComponent = effectComponent.hoverEvent(Component.text(effect.getDescriptionBlurb()).color(color));
                    commandSender.sendMessage(effectComponent);
                }
            }
        }
        if (args[1].equalsIgnoreCase("SendNewPrompt")) {
            if (!WYREventHandler.isActive() || !MinigamesWithFriends.getGame().isGamemodeActive(WouldYouRatherGamemode.class)) {
                commandSender.sendMessage("§cThe Would You Rather gamemode is not active!");
                return;
            }
            MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).onTimeEventTrigger();
        }
    }

    private static void handleDevCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("preLoadSavedDimensionSwapWorlds")) {
            DimensionSwapGamemode.preLoadSavedWorlds(commandSender);
            commandSender.sendMessage("§aPreloaded saved worlds.");
        } else if (args[1].equalsIgnoreCase("launchToSpawn")) {
            Player plr = (Player) commandSender;
            PlayerUtils.launchPlayerToLoc(plr, plr.getWorld().getSpawnLocation());
        } else if (args[1].equalsIgnoreCase("SafeLoc")) {
            Player plr = (Player) commandSender;
            plr.setFallDistance(0);
            plr.teleport(BlockUtils.findSafeBlock(plr.getLocation()));
        } else if (args[1].equalsIgnoreCase("fling")) {
            Player plr = (Player) commandSender;
            Game.launchPlayerSideways(plr, 10);
        }
    }

    private static void handleBlockShuffleCommand(@NotNull CommandSender commandSender, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("BanBlock")) {
            if (args.length == 3) {
                String blockName = args[2];
                Material block = Material.getMaterial(blockName);
                if (block == null) {
                    commandSender.sendMessage("§cInvalid block name. Must be uppercase enum style");
                    return;
                }
                BlockShuffleGamemode.banBlock(block);
                commandSender.sendMessage("§aBlock " + blockName + " banned.");
            } else {
                commandSender.sendMessage("§cPlease specify a block name.");
            }
        } else if (args[1].equalsIgnoreCase("UnbanBlock")) {
            if (args.length == 3) {
                String blockName = args[2];
                Material block = Material.getMaterial(blockName);
                if (block == null) {
                    commandSender.sendMessage("§cInvalid block name. Must be uppercase enum style");
                    return;
                }
                BlockShuffleGamemode.unbanBlock(block);
                commandSender.sendMessage("§aBlock " + blockName + " unbanned.");
            } else {
                commandSender.sendMessage("§cPlease specify a block name.");
            }
        } else if (args[1].equalsIgnoreCase("ListBannedBlocks")) {
            commandSender.sendMessage("§eBanned blocks: §b");
            for (String materialName : BlockShuffleGamemode.getBannedBlocksStringList()) {
                commandSender.sendMessage("§b" + materialName);
            }
        } else if (args[1].equalsIgnoreCase("skip")) {
            if (!MinigamesWithFriends.getGame().isRunning() || !MinigamesWithFriends.getGame().isGamemodeActive(BlockShuffleGamemode.class)) {
                return;
            }
            MinigamesWithFriends.getGame().getGamemode(BlockShuffleGamemode.class).skip();
        }
    }

    private void handleDisableGamemode(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            String moduleName = args[1];
            if (!Gamemode.isValidGamemode(moduleName)) {
                commandSender.sendMessage("§cInvalid gamemode name.");
                commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
                return;
            }
            MinigamesWithFriends.getGame().removeGamemode(Gamemode.getGamemodeFromName(moduleName).getClass());
            commandSender.sendMessage("§aGamemode " + moduleName + " disabled.");
        } else {
            commandSender.sendMessage("§cPlease specify a module name.");
            commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
        }
    }

    private void handleEnableGamemode(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            String moduleName = args[1];
            if (!Gamemode.isValidGamemode(moduleName)) {
                commandSender.sendMessage("§cInvalid gamemode name.");
                commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
                return;
            }
            if (MinigamesWithFriends.getGame().isGamemodeActive(Gamemode.getGamemodeFromName(moduleName).getClass())) {
                commandSender.sendMessage("§cGamemode " + moduleName + " is already enabled.");
                return;
            }
            MinigamesWithFriends.getGame().addGamemode(Gamemode.getGamemodeFromName(moduleName));
            commandSender.sendMessage("§aGamemode " + moduleName + " enabled.");
        } else {
            commandSender.sendMessage("§cPlease specify a module name.");
            commandSender.sendMessage("§eValid options are: §b" + Gamemode.getGamemodeListString());
        }
    }

    private void handleSettingConfigValue(CommandSender commandSender, String configName, String configKey, String value) {
        GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        Class<?> type = conf.getConfigValues().get(configKey);
        if (type == null) {
            commandSender.sendMessage("§cConfig key not found.");
            return;
        }
        boolean wasValid = conf.validateAndSetValue(configKey, value);
        if (!wasValid) {
            commandSender.sendMessage("§cInvalid value type. Expected: " + type.getSimpleName());
            return;
        }
        commandSender.sendMessage("§aConfig: " + configKey + " set to " + value);
        MinigamesWithFriends.getGame().updateConfig();
    }

    private void handleFetchingConfigValue(CommandSender commandSender, String configName, String configKey) {
        GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        String value = conf.getString(configKey);
        if (value == null) {
            commandSender.sendMessage("§cConfig key not found.");
            return;
        }
        commandSender.sendMessage("§aConfig: " + configKey + " = " + value);
    }

    private void sendValidGamemodeNames(CommandSender commandSender) {
        commandSender.sendMessage("""
                §cGamemode not found,valid options are:
                §dmainGame
                §ddimensionSwap""");
    }

    private void handleConfig2Args(CommandSender commandSender, String configName) {
        GamemodeConfig conf = MinigamesWithFriends.getGame().getConfig().getGamemodeConfigFromName(configName);
        if (conf == null) {
            sendValidGamemodeNames(commandSender);
            return;
        }
        commandSender.sendMessage("§eValid config values:");
        for (String configKey : conf.getConfigValues().keySet()) {
            String type = conf.getConfigValues().get(configKey).getSimpleName();
            String value = conf.getString(configKey);
            commandSender.sendMessage("§d" + configKey + " = " + value + " §e: " + type);
        }
    }
}
