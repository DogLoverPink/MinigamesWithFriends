package doglover.minigameswithfriends;

import doglover.minigameswithfriends.configs.MainGameConfig;
import doglover.minigameswithfriends.gamemodes.Gamemode;
import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.ParticleUtils;
import doglover.minigameswithfriends.utils.PlayerUtils;
import fr.mrmicky.fastboard.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class Game {
    private final Set<UUID> players = new HashSet<>();
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

    public Set<Player> getPlayers() {
        Set<Player> onlinePlayers = new HashSet<>();
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
        FastBoard board = boards.get(player.getUniqueId());
        if (board != null) {
            board.delete();
            boards.remove(player.getUniqueId());
        }
    }

    public List<Gamemode> getGamemodes() {
        return gamemodes;
    }

    public List<String> getGamemodesAsString() {
        List<String> gamemodeNames = new ArrayList<>();
        for (Gamemode gamemode : gamemodes) {
            gamemodeNames.add(gamemode.getClass().getSimpleName().replace("Gamemode", ""));
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
            gamemode.onGameStart();
            broadcast("§b" + gamemode.getClass().getSimpleName() + " §ahas been enabled.");
        }
    }

    public void removeGamemode(Class<? extends Gamemode> gamemodeClazz) {
        for (Gamemode gamemode : gamemodes) {
            if (gamemode.getClass() == gamemodeClazz) {
                if (isRunning) {
                    gamemode.onGameEnd();
                    broadcast("§b" + gamemode.getClass().getSimpleName() + " §ahas been disabled");
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
            if (gamemode.getClass() == gamemodeClass) {
                return (T) gamemode;
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
                p.sendMessage("§a§l" + player.getName() + " has won the game!");
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
        if (isRunning && players.contains(player.getUniqueId())) {
            FastBoard board = new FastBoard(player);
            boards.put(player.getUniqueId(), board);
            board.updateTitle("§b§lMinigames with Friends");
            board.updateLines(new ArrayList<>());
            player.sendMessage("§aContinued game in progress!");
            player.sendMessage("§aEnabled gamemodes: §b" + this.getGamemodes().toString().replace("[", "").replace("]", ""));
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


        if (players.isEmpty()) {
            players.addAll(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getUniqueId).toList());
        }
        pointsToWin = config.getPointsToWin();
        isRunning = true;
        inDeathMatch = false;
        aliveDeathMatchPlayers.clear();
        deadDeathMatchPlayers.clear();

        if (getConfig().shouldSetToDayOnStart()) {
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
            FastBoard board = new FastBoard(player);


            boards.put(player.getUniqueId(), board);
            board.updateTitle("§b§lMinigames with Friends");
            board.updateLines(new ArrayList<>());

            PlayerUtils.resetPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
            if (getConfig().shouldTeleportPlayersToWorldSpawnOnGameStart()) {
                player.teleport(player.getWorld().getSpawnLocation());
            }
            player.sendMessage("§a§lGame Started!");
            player.sendMessage("§aEnabled gamemodes: §b" + this.getGamemodes().toString().replace("[", "").replace("]", ""));

        }
        for (Gamemode gamemode : gamemodes) {
            gamemode.onGameStart();
        }
    }

    public void endGame() {
        isRunning = false;
        for (Gamemode gamemode : gamemodes) {
            gamemode.onGameEnd();
        }
        for (Player player : getPlayers()) {
            FastBoard board = boards.get(player.getUniqueId());
            if (board != null) {
                board.delete();
            }
        }
        if (isInDeathMatch()) {
            Location dmloc = deathmatchWorld.getSpawnLocation();
            BlockUtils.removeWallsAroundLocation(dmloc, getConfig().getDeathMatchConfig().getDeathmatchAreaRadiusBlocks());
        }
        players.clear();
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
        inDeathMatch = true;
        aliveDeathMatchPlayers.clear();
        aliveDeathMatchPlayers.addAll(players);
        deadDeathMatchPlayers.clear();


        List<Player> playersList = new ArrayList<>(getPlayers());
        Player randomPlayer = playersList.get(new Random().nextInt(playersList.size()));
        Location loc = BlockUtils.findSafeBlock(randomPlayer.getWorld().getSpawnLocation());
        deathmatchWorld = loc.getWorld();
        BlockUtils.createWallsAroundLocation(loc, getConfig().getDeathMatchConfig().getDeathmatchAreaRadiusBlocks());
        BlockUtils.createHollowBoxAroundLocation(loc);
        for (Player player : getPlayers()) {
            previousLocations.put(player.getUniqueId(), player.getLocation());
            player.teleport(loc);
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.sendMessage("§c§lDeathmatch Started!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 12 * 20, 5, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 5 * 20, 5, true, false));
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
                    player.showTitle(Title.title(Component.text("§a" + i), Component.text("§eGet Ready!")));
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
            BlockUtils.removeWallsAroundLocation(dmloc, getConfig().getDeathMatchConfig().getDeathmatchAreaRadiusBlocks());
            aliveDeathMatchPlayers.clear();
            deadDeathMatchPlayers.clear();
            inDeathMatch = false;
            return;
        }

        Location dmloc = deathmatchWorld.getSpawnLocation();
        BlockUtils.removeWallsAroundLocation(dmloc, getConfig().getDeathMatchConfig().getDeathmatchAreaRadiusBlocks());


        for (Player plr : getPlayers()) {
            plr.sendMessage("§a" + winnerName + " won deathmatch! ");
            plr.setHealth(20.0);
            Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
                Location loc = previousLocations.get(plr.getUniqueId());
                plr.setGameMode(GameMode.SURVIVAL);
                if (loc != null) {
                    plr.teleport(loc);
                    plr.setGameMode(GameMode.SURVIVAL);
                }
            }, 59);
            plr.sendMessage("§c§lDeathmatch Ended!");
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
            p.sendMessage("§4☠§c" + player.getName() + " has died!");
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
            int deathMatchRadius = getConfig().getDeathMatchConfig().getDeathmatchAreaRadiusBlocks();
            if (xDistance >= deathMatchRadius || zDistance >= deathMatchRadius) {
                if (xDistance - 3 >= deathMatchRadius || zDistance - 3 >= deathMatchRadius) {
                    BlockUtils.removeBlockOfTypeNearThenReplace(player.getLocation(), Material.AIR, (Material[]) null);
                } else {
                    BlockUtils.removeBlockOfTypeNearThenReplace(player.getLocation(), Material.AIR, Material.RED_WOOL);
                }
                PlayerUtils.launchPlayerToLoc(player, worldSpawn);
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 3, 1, true, false));
                player.sendMessage("§cnuh uh uh");
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
        for (UUID uuid : players) {
            FastBoard board = boards.get(uuid);
            if (board != null) {
                board.updateLines(lines);
            }
        }
    }

    private final Map<String, TimedActionBar> actionBars = new LinkedHashMap<>();

    private record TimedActionBar(String key, Component text, int serverTickEndTime, Player player) {

    }

    public void sendActionBar(String key, Component text, Player player) {
        sendActionBar(key, text, player, 60);
    }

    public void sendActionBar(String key, Component text, Player player, int durationTicks) {
        durationTicks = durationTicks + Bukkit.getCurrentTick();
        actionBars.put(key, new TimedActionBar(key, text, durationTicks, player));
    }

    public void tick() {
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
    }


}
