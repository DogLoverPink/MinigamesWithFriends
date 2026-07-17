package doglover.minigameswithfriends;

import doglover.minigameswithfriends.configs.MainGameConfig;
import doglover.minigameswithfriends.gamemodes.DeathmatchGamemode;
import doglover.minigameswithfriends.gamemodes.Gamemode;
import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.ParticleUtils;
import doglover.minigameswithfriends.utils.PlayerUtils;
import doglover.minigameswithfriends.utils.TextUtils;
import fr.mrmicky.fastboard.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Game {
    private final Set<UUID> players = new HashSet<>();
    private final Set<UUID> spectators = new HashSet<>();
    private final Map<Player, Integer> points = new HashMap<>();
    private int pointsToWin = 2;

    private final MainGameConfig config = new MainGameConfig();

    public MainGameConfig getConfig() {
        return config;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private static final Random random = new Random();

    public static Random getRandom() {
        return random;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private List<Gamemode> gamemodes = new ArrayList<>();
    private boolean isRunning;

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public Set<Player> getPlayersAndSpectators() {
        return Stream.concat(getPlayers().stream(), getSpectators().stream()).collect(Collectors.toSet());
    }

    private void invalidateCaches() {
        getPlayersAndSpectators().clear();
        getSpectators().clear();
        getPlayersCache = null;
        getSpectatorsCache = null;
    }

    private Set<Player> getPlayersCache = null;
    private Set<Player> getSpectatorsCache = null;

    public Set<Player> getPlayers() {
        if (getPlayersCache != null) {
            return getPlayersCache;
        }
        Set<Player> onlinePlayers = new HashSet<>();
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                onlinePlayers.add(player);
            }
        }
        getPlayersCache = onlinePlayers;
        return getPlayersCache;
    }

    public Set<Player> getSpectators() {
        if (getSpectatorsCache != null) {
            return getSpectatorsCache;
        }
        Set<Player> spectatorsList = new HashSet<>();
        for (UUID uuid : spectators) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                spectatorsList.add(player);
            }
        }
        getSpectatorsCache = spectatorsList;
        return getSpectatorsCache;
    }

    public boolean isPlayerInGame(Player plr) {
        return !players.contains(plr.getUniqueId());
    }

    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
        invalidateCaches();
        if (isRunning) {
            for (Gamemode gamemode : gamemodes) {
                gamemode.onPlayerJoin(player);
            }
        }
    }

    public void addSpectator(Player player) {
        if (isRunning) {
            if (!spectators.contains(player.getUniqueId())) {
                for (Player plr : getPlayersAndSpectators()) {
                    plr.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<aqua>" + player.getName() + "<green> is now spectating!"));
                }
            }
            player.setGameMode(GameMode.SPECTATOR);
            MinigamesWithFriends.getGame().removePlayer(player);

            this.spectators.add(player.getUniqueId());
            invalidateCaches();

            //Remove "ghost" scoreboard contribution, as the game won't tick anymore to remove it
            if (scoreboardContributions.size() >= 2 && getPlayers().isEmpty()) {
                if (scoreboardContributions.get(scoreboardContributions.size() - 2).startsWith("§dPoints:")) {
                    scoreboardContributions.removeLast();
                    scoreboardContributions.removeLast();
                    displayOnBoard(scoreboardContributions);

                }
            }
        } else {
            this.spectators.add(player.getUniqueId());
            invalidateCaches();
        }
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
        invalidateCaches();
        if (isRunning) {
            for (Gamemode gamemode : gamemodes) {
                gamemode.onPlayerLeave(player);
            }
        }
    }

    public void removeSpectator(Player player) {
        this.spectators.remove(player.getUniqueId());
        addPlayer(player);
        if (isRunning) {
            player.teleport(player.getWorld().getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
        }
        invalidateCaches();
    }


    public List<Gamemode> getGamemodes() {
        return gamemodes;
    }

    public List<String> getGamemodesAsString() {
        List<String> gamemodeNames = new ArrayList<>();
        for (Gamemode gamemode : gamemodes) {
            gamemodeNames.add(Gamemode.getGamemodeNameFromClass(gamemode.getClass()));
        }
        return gamemodeNames;
    }

    protected void setGamemodes(List<Gamemode> gamemodes) {
        this.gamemodes = gamemodes;
    }

    public void addGamemode(Gamemode gamemode) {
        this.gamemodes.add(gamemode);
        gamemode.setGame(this);
        if (isRunning) {
            gamemode.registerSubscribedEvents();
            gamemode.onGameStart();
            broadcast(TextUtils.MINI_MESSAGE.deserialize("<aqua>" + gamemode.getClass().getSimpleName() + " <green>has been enabled."));
        }
    }

    public void removeGamemode(Class<? extends Gamemode> gamemodeClazz) {
        for (Gamemode gamemode : gamemodes) {
            if (gamemode.getClass() == gamemodeClazz) {
                if (isRunning) {
                    gamemode.onGameEnd();
                    gamemode.unregisterSubscribedEvents();
                    broadcast(TextUtils.MINI_MESSAGE.deserialize("<aqua>" + gamemode.getClass().getSimpleName() + " <green>has been disabled"));
                }
                this.gamemodes.remove(gamemode);
                gamemode.setGame(null);
                return;
            }
        }
    }

    public void clearGamemodes() {
        List<Class<? extends Gamemode>> gamemodes = new ArrayList<>();
        for (Gamemode gamemode : this.gamemodes) {
            gamemodes.add(gamemode.getClass());
        }
        for (Class<? extends Gamemode> gamemode : gamemodes) {
            removeGamemode(gamemode);
        }
    }

    public boolean isGamemodeActive(Class<? extends Gamemode> gamemodeClass) {
        for (Gamemode gamemode : gamemodes) {
            if (gamemode.getClass() == gamemodeClass) {
                return true;
            }
        }
        return false;
    }


    public <T extends Gamemode> T getGamemode(Class<T> gamemodeClass) {
        for (Gamemode gamemode : gamemodes) {
            if (gamemodeClass.isInstance(gamemode)) {
                return gamemodeClass.cast(gamemode);
            }
        }
        return null;
    }

    public void addPointsToPlayer(Player player, int points) {
        if (this.points.containsKey(player)) {
            this.points.put(player, this.points.get(player) + points);
        } else {
            this.points.put(player, points);
        }
        if (this.points.get(player) >= pointsToWin) {
            for (Player p : getPlayers()) {
                p.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green><bold>" + player.getName() + " has won the game!"));
                p.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                ParticleUtils.createParticleCloud(p.getLocation(), 3, Particle.FIREWORK, 30);
                p.showTitle(Title.title(Component.text(player.getName() + " won!!").color(NamedTextColor.GOLD), Component.empty(), 8, 50, 20));
            }
            //prevents concurrent modification exception
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), this::endGame, 0);
        }
    }

    public int getPointsFromPlayer(Player player) {
        return this.points.getOrDefault(player, 0);
    }

    public void broadcast(String message) {
        for (Player player : getPlayers()) {
            player.sendMessage(message);
        }
    }

    public void broadcast(Component messageComponent) {
        for (Player player : getPlayers()) {
            player.sendMessage(messageComponent);
        }
    }

    public void reportPlayerQuit(Player player) {
        if (isRunning) {
            boards.remove(player.getUniqueId());
        }
    }

    public void reportPlayerJoin(Player player) {
        if (!isRunning) {
            return;
        }
        if (players.contains(player.getUniqueId())) {
            setupBoardForPlayer(player);
            player.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Continued game in progress!"));
            player.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Enabled gamemodes: <aqua>" + this.getGamemodes().toString().replace("[", "").replace("]", "")));
        } else {
            setupBoardForPlayer(player);
            addSpectator(player);
            player.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<aqua>A game is in progress, you are now spectating!"));
            String name = player.getName();
            Component message = TextUtils.MINI_MESSAGE.deserialize("<yellow>If you wish to play the game instead, have you or an admin run</yellow> <hover:show_text:'<aqua>Click to join game!</aqua>'><click:run_command:'/mg RemoveSpectator " + name + "'><aqua>/</aqua><green>mg RemoveSpectator " + name + "</green><aqua> (Click me)</aqua></click></hover>");
            player.sendMessage(message);
        }
    }

    public void updateConfig() {
        pointsToWin = config.getPointsToWin();
        if (isRunning) {
            for (Gamemode gamemode : gamemodes) {
                gamemode.updateConfig();
            }
        }
    }

    public void startGame() {


        players.clear();
        players.addAll(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getUniqueId).toList());
        players.removeAll(spectators);
        invalidateCaches();


        pointsToWin = config.getPointsToWin();
        isRunning = true;
        inDeathMatch = false;
        aliveDeathMatchPlayers.clear();
        deadDeathMatchPlayers.clear();

        if (getConfig().shouldSetToDayOnGameStart()) {
            for (Player player : getPlayers()) {
                player.getWorld().setTime(1000);
            }
        }

        for (Player player : getPlayers()) {

            player.spigot().respawn();

            if (config.shouldResetAdvancementsOnGameStart()) {
                Bukkit.getServer().advancementIterator().forEachRemaining(advancement -> {
                    for (String criteria : player.getAdvancementProgress(advancement).getAwardedCriteria()) {
                        player.getAdvancementProgress(advancement).revokeCriteria(criteria);
                    }
                });
            }

            points.put(player, 0);


            PlayerUtils.resetPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
            if (getConfig().shouldTeleportPlayersToWorldSpawnOnGameStart()) {
                player.teleport(player.getWorld().getSpawnLocation());
            }


        }

        for (Player everyone : getPlayersAndSpectators()) {

            setupBoardForPlayer(everyone);
            everyone.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green><bold>Game Started!"));
            everyone.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>Enabled gamemodes: <aqua>" + this.getGamemodes().toString().replace("[", "").replace("]", "")));

        }

        for (Player spectator : getSpectators()) {
            spectator.setGameMode(GameMode.SPECTATOR);
            spectator.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<yellow>You are <aqua>spectating"));
        }
        for (Gamemode gamemode : gamemodes) {
            gamemode.registerSubscribedEvents();
            gamemode.onGameStart();
        }
    }

    private void setupBoardForPlayer(Player plr) {
        FastBoard board = new FastBoard(plr);
        boards.put(plr.getUniqueId(), board);
        board.updateTitle("§b§lMinigames with Friends");
        board.updateLines(scoreboardContributions);
    }

    public void endGame() {
        isRunning = false;
        for (Gamemode gamemode : gamemodes) {
            gamemode.onGameEnd();
            gamemode.unregisterSubscribedEvents();
        }
        for (Player player : getPlayersAndSpectators()) {
            FastBoard board = boards.get(player.getUniqueId());
            if (board != null) {
                board.delete();
            }
        }
        if (isInDeathMatch()) {
            Location dmloc = deathmatchWorld.getSpawnLocation();
            BlockUtils.removeWallsAroundLocation(dmloc, DeathmatchGamemode.config().getDeathmatchAreaRadiusBlocks());
        }
        players.clear();
        spectators.clear();
        boards.clear();
        points.clear();
        gamemodes.clear();

    }

    private final List<String> scoreboardContributions = new ArrayList<>();

    public void addScoreboardContribution(String contribution) {
        this.scoreboardContributions.add(contribution);
    }

    public boolean isInDeathMatch() {
        return inDeathMatch;
    }

    public void setInDeathMatch(boolean inDeathMatch) {
        this.inDeathMatch = inDeathMatch;
    }


    private boolean inDeathMatch = false;

    List<UUID> aliveDeathMatchPlayers = new ArrayList<>();
    List<UUID> deadDeathMatchPlayers = new ArrayList<>();
    Map<UUID, Location> previousLocations = new HashMap<>();


    World deathmatchWorld;

    public void startDeathMatch() {

        if (getPlayers().size() <= 1) {
            getPlayersAndSpectators().forEach(player -> {
                player.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>Not enough players for deathmatch! Skipping"));
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
            });
            if (getPlayers().size() == 1) {
                deathmatchWorld = getPlayers().stream().toList().getFirst().getWorld();
            } else {
                deathmatchWorld = Bukkit.getWorld("world");
            }
            endDeathMatch(null);
            return;
        }

        inDeathMatch = true;
        aliveDeathMatchPlayers.clear();
        aliveDeathMatchPlayers.addAll(players);
        deadDeathMatchPlayers.clear();


        List<Player> playersList = new ArrayList<>(getPlayers());
        Player randomPlayer = playersList.get(new Random().nextInt(playersList.size()));
        Location loc = BlockUtils.findSafeBlock(randomPlayer.getWorld().getSpawnLocation());
        deathmatchWorld = loc.getWorld();
        BlockUtils.createWallsAroundLocation(loc, DeathmatchGamemode.config().getDeathmatchAreaRadiusBlocks());
        BlockUtils.createSafePlatformIfNotExist(loc, DeathmatchGamemode.config().getDeathmatchAreaRadiusBlocks());
        BlockUtils.createHollowBoxAroundLocation(loc);
        for (Player player : getPlayers()) {
            previousLocations.put(player.getUniqueId(), player.getLocation());
            player.teleport(loc);
            player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
            player.setFoodLevel(20);
            player.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red><bold>Deathmatch Started!"));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 10 * 20, 4, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 5 * 20, 9, true, false));
            BukkitRunnable runnable = new BukkitRunnable() {
                int i = 5;

                @Override
                public void run() {


                    if (i == 0) {
                        player.clearTitle();
                        launchPlayerSideways(player, 8.5);
                        this.cancel();
                        return;
                    }
                    player.showTitle(Title.title(
                            TextUtils.MINI_MESSAGE.deserialize("<green>" + i),
                            TextUtils.MINI_MESSAGE.deserialize("<yellow>Get Ready!")));
                    i--;
                }

            };
            runnable.runTaskTimer(MinigamesWithFriends.getGamePlugin(), 0, 20);
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
                MinigamesWithFriends.getGamePlugin().getLogger().info("Removing walls around location: " + loc);
                BlockUtils.removeHollowBoxAroundLocation(loc);
            }, 98);
        }
    }

    public static void launchPlayerSideways(Player player, double strength) {
        double angle = random.nextDouble() * 2 * Math.PI; // Radians, 0 to 2π

        double x = Math.cos(angle);
        double z = Math.sin(angle);
        org.bukkit.util.Vector direction = new org.bukkit.util.Vector(x, 0.1, z).normalize().multiply(strength);

        player.setVelocity(direction);
    }

    private void endDeathMatch(Player winner) {

        String winnerName = "No one";
        if (winner != null) {
            addPointsToPlayer(winner, config.getPointsPerDeathmatchWin());
            winnerName = winner.getName();
        }


        if (!isRunning) {
            Location dmloc = deathmatchWorld.getSpawnLocation();
            BlockUtils.removeWallsAroundLocation(dmloc, DeathmatchGamemode.config().getDeathmatchAreaRadiusBlocks());
            aliveDeathMatchPlayers.clear();
            deadDeathMatchPlayers.clear();
            inDeathMatch = false;
            return;
        }

        Location dmloc = deathmatchWorld.getSpawnLocation();
        BlockUtils.removeWallsAroundLocation(dmloc, DeathmatchGamemode.config().getDeathmatchAreaRadiusBlocks());


        for (Player plr : getPlayers()) {
            plr.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<green>" + winnerName + " won deathmatch! "));
            plr.setHealth(plr.getAttribute(Attribute.MAX_HEALTH).getValue());
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
                Location loc = previousLocations.get(plr.getUniqueId());
                plr.setGameMode(GameMode.SURVIVAL);
                if (loc != null) {
                    plr.teleport(loc);
                    plr.setGameMode(GameMode.SURVIVAL);
                }
            }, 59);
            plr.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red><bold>Deathmatch Ended!"));
        }

        for (Gamemode gamemode : gamemodes) {
            gamemode.onDeathMatchEnd();
        }

        Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> inDeathMatch = false, 60);
        aliveDeathMatchPlayers.clear();
        deadDeathMatchPlayers.clear();
    }

    public void reportDeathmatchDeath(Player player) {
        aliveDeathMatchPlayers.remove(player.getUniqueId());
        deadDeathMatchPlayers.add(player.getUniqueId());
        for (Player p : getPlayers()) {
            p.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<dark_red>☠<red>" + player.getName() + " has died!"));
        }
        if (aliveDeathMatchPlayers.size() == 1) {
            Player winner = Bukkit.getPlayer(aliveDeathMatchPlayers.getFirst());
            endDeathMatch(winner);
        } else if (aliveDeathMatchPlayers.isEmpty()) {
            endDeathMatch(null);
        }
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void tickDeathMatch() {
        addScoreboardContribution("§c§lDeathmatch");
        for (UUID uuid : aliveDeathMatchPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                continue;
            }
            addScoreboardContribution("§c♡§a" + player.getName());
            if (!player.getWorld().equals(deathmatchWorld)) {
                player.teleport(deathmatchWorld.getSpawnLocation());
            }
            Location worldSpawn = player.getWorld().getSpawnLocation();
            double xDistance = Math.abs(player.getLocation().getX() - worldSpawn.getX());
            double zDistance = Math.abs(player.getLocation().getZ() - worldSpawn.getZ());
            int deathMatchRadius = DeathmatchGamemode.config().getDeathmatchAreaRadiusBlocks();
            if (xDistance >= deathMatchRadius || zDistance >= deathMatchRadius) {
                if (xDistance - 3 >= deathMatchRadius || zDistance - 3 >= deathMatchRadius) {
                    BlockUtils.removeBlockOfTypeNearThenReplace(player.getLocation(), Material.AIR, (Material[]) null);
                } else {
                    BlockUtils.removeBlockOfTypeNearThenReplace(player.getLocation(), Material.AIR, Material.RED_WOOL);
                }
                PlayerUtils.launchPlayerToLoc(player, worldSpawn);
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 3, 1, true, false));
                player.sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>nuh uh uh"));
            }
        }
        for (UUID uuid : deadDeathMatchPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                continue;
            }
            addScoreboardContribution("§4☠§c" + player.getName());
        }
        displayOnBoard(scoreboardContributions);
    }


    private void displayOnBoard(List<String> lines) {
        for (Player plr : getPlayersAndSpectators()) {

            FastBoard board = boards.get(plr.getUniqueId());
            if (board != null) {
                board.updateLines(lines);
            }
        }
    }

    private final Map<String, TimedActionBar> actionBars = new LinkedHashMap<>();

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private record TimedActionBar(String key, Component text, int serverTickEndTime, Player player) {

    }

    public void sendActionBar(String key, Component text, Player player) {
        sendActionBar(key, text, player, 60);
    }

    public void sendActionBar(String key, Component text, Player player, int durationTicks) {
        durationTicks = durationTicks + Bukkit.getCurrentTick();
        actionBars.put(key, new TimedActionBar(key, text, durationTicks, player));
    }

    private boolean paused = false;


    public void tick() {
        if (isPaused()) {
            return;
        }
        if (getPlayers().isEmpty()) {
            invalidateCaches();
            return;
        }
        for (Player plr : getPlayers()) {
            Component actionBarString = Component.empty();
            List<TimedActionBar> actionBarList = actionBars.values().stream().filter(actionBar -> actionBar.player.equals(plr)).toList();
            for (TimedActionBar actionBar : actionBarList) {
                actionBarString = actionBarString.append(actionBar.text());
                if (!actionBar.equals(actionBarList.getLast())) {
                    actionBarString = actionBarString.append(Component.text(" | "));
                }
            }
            plr.sendActionBar(actionBarString);
        }
        actionBars.values().removeIf(actionBar -> actionBar.serverTickEndTime() < Bukkit.getCurrentTick());
        if (inDeathMatch) {
            if (isGamemodeActive(WouldYouRatherGamemode.class)) {
                getGamemode(WouldYouRatherGamemode.class).tick();
            }
            scoreboardContributions.clear();
            tickDeathMatch();
            return;
        }
        scoreboardContributions.clear();
        for (Gamemode gamemode : gamemodes) {
            gamemode.tick();
        }
        scoreboardContributions.add("§dPoints: (First to " + pointsToWin + ")");
        List<Player> sortedPlayerPoints = new ArrayList<>(getPlayers());
        sortedPlayerPoints.sort(Comparator.comparingInt(this::getPointsFromPlayer).reversed());
        for (Player player : sortedPlayerPoints) {
            scoreboardContributions.add("§b" + player.getName() + ": §d" + getPointsFromPlayer(player));
        }
        displayOnBoard(scoreboardContributions);
        invalidateCaches();
    }


}
