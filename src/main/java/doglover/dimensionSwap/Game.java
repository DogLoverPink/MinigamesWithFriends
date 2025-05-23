package doglover.dimensionSwap;

import doglover.dimensionSwap.gamemodes.Gamemode;
import doglover.dimensionSwap.utils.BlockUtils;
import fr.mrmicky.fastboard.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class Game {
    private Set<Player> players = new HashSet<>();
    private Map<Player, Integer> points = new HashMap<>();
    private int pointsToWin = 2;
    public boolean isRunning() {
        return isRunning;
    }

    private static final Random random = new Random();

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private List<Gamemode> gamemodes = new ArrayList<>();
    private boolean isRunning;

    private final Map<Player, FastBoard> boards = new HashMap<>();

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
        FastBoard board = boards.get(player);
        if (board != null) {
            board.delete();
            boards.remove(player);
        }
    }

    public List<Gamemode> getGamemodes() {
        return gamemodes;
    }
    public void setGamemodes(List<Gamemode> gamemodes) {
        this.gamemodes = gamemodes;
    }
    public void addGamemode(Gamemode gamemode) {
        this.gamemodes.add(gamemode);
        gamemode.setGame(this);
    }
    public void removeGamemode(Gamemode gamemode) {
        this.gamemodes.remove(gamemode);
        gamemode.setGame(null);
    }

    public void clearGamemodes() {
        for (Gamemode gamemode : gamemodes) {
            gamemode.setGame(null);
        }
        this.gamemodes.clear();
    }

    public void addPointsToPlayer(Player player, int points) {
        if (this.points.containsKey(player)) {
            this.points.put(player, this.points.get(player) + points);
        } else {
            this.points.put(player, points);
        }
        if (this.points.get(player) >= pointsToWin) {
            for (Player p : players) {
                p.sendMessage("§a§l" + player.getName() + " has won the game!");
            }
            endGame();
        }
    }

    public int getPointsFromPlayer(Player player) {
        return this.points.getOrDefault(player, 0);
    }

    public void startGame() {

        if (players.isEmpty()) {
            players.addAll(Bukkit.getServer().getOnlinePlayers());
        }

        isRunning = true;
        for (Player player : players) {

            player.spigot().respawn();

            points.put(player, 0);
            FastBoard board = new FastBoard(player);


            boards.put(player, board);
            board.updateTitle("§b§lEpic Minigames");
            board.updateLines(new ArrayList<>());

            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.setHealth(20.0);

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
        for (Player player : players) {
            FastBoard board = boards.get(player);
            if (board != null) {
                board.delete();
            }
        }
        players.clear();
        boards.clear();
        points.clear();

    }

    private List<String> scoreboardContribututions = new ArrayList<>();

    public void addScoreboardContributution(String contributution) {
        this.scoreboardContribututions.add(contributution);
    }

    public boolean isInDeathMatch() {
        return inDeathMatch;
    }

    public void setInDeathMatch(boolean inDeathMatch) {
        this.inDeathMatch = inDeathMatch;
    }

    private boolean inDeathMatch = false;

    List<Player> aliveDeathMatchPlayers = new ArrayList<>();
    List<Player> deadDeathMatchPlayers = new ArrayList<>();
    Map<Player, Location> previousLocations = new HashMap<>();


    public void startDeathMatch() {
        inDeathMatch = true;
        aliveDeathMatchPlayers.clear();
        aliveDeathMatchPlayers.addAll(players);
        deadDeathMatchPlayers.clear();


        List<Player> playersList = new ArrayList<>(players);
        Player randomPlayer = playersList.get(new Random().nextInt(playersList.size()));
        Location loc = randomPlayer.getWorld().getSpawnLocation();
        BlockUtils.createWallsAroundLocation(loc);
        BlockUtils.createHollowBoxAroundLocation(loc);
        for (Player player : players) {
            previousLocations.put(player, player.getLocation());
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
                        launchPlayerSideways(player, 12.5);
                        this.cancel();
                        return;
                    }
                    player.showTitle(Title.title(Component.text("§a" + i), Component.text("§eGet Ready!")));
                    i--;
                };
                };
            runnable.runTaskTimer(DimensionSwap.getGamePlugin(), 0, 20);
            Bukkit.getScheduler().runTaskLater(DimensionSwap.getGamePlugin(), () -> {
                    DimensionSwap.getGamePlugin().getLogger().info("Removing walls around location: " + loc.toString());
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

        addPointsToPlayer(winner, 1);

        if (!isRunning) {
            Location dmloc = winner.getWorld().getSpawnLocation();
            BlockUtils.removeWallsAroundLocation(dmloc);
            aliveDeathMatchPlayers.clear();
            deadDeathMatchPlayers.clear();
            inDeathMatch = false;
            return;
        }

        Location dmloc = winner.getWorld().getSpawnLocation();
        BlockUtils.removeWallsAroundLocation(dmloc);


        for (Player plr: players) {
            plr.sendMessage("§a§l" + winner.getName() + " is the winner!");
            plr.setHealth(20.0);
            Bukkit.getScheduler().runTaskLater(DimensionSwap.getGamePlugin(), () -> {
                Location loc = previousLocations.get(plr);
                plr.setGameMode(GameMode.SURVIVAL);
                if (loc != null) {
                    plr.teleport(loc);
                    plr.setGameMode(GameMode.SURVIVAL);
                }
            }, 59);
            plr.sendMessage("§c§lDeathmatch Ended!");
        }

        Bukkit.getScheduler().runTaskLater(DimensionSwap.getGamePlugin(), () -> {
            inDeathMatch = false;
        }, 60);
        aliveDeathMatchPlayers.clear();
        deadDeathMatchPlayers.clear();
    }

    public void reportDeathmatchDeath(Player player) {
        aliveDeathMatchPlayers.remove(player);
        deadDeathMatchPlayers.add(player);
        for (Player p : players) {
            p.sendMessage("§4☠§c" + player.getName() + " has died!");
        }
        if (aliveDeathMatchPlayers.size() == 1) {
            Player winner = aliveDeathMatchPlayers.getFirst();
            endDeathMatch(winner);
        }
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void tickDeathMatch() {
        addScoreboardContributution("§c§lDeathmatch");
        for (Player player : aliveDeathMatchPlayers) {
            addScoreboardContributution("§c♡§a" + player.getName());
        }
        for (Player player : deadDeathMatchPlayers) {
            addScoreboardContributution("§4☠§c" + player.getName());
        }
        displayOnBoard(scoreboardContribututions);
    }



    private void displayOnBoard(List<String> lines) {
        for (Player player : players) {
            FastBoard board = boards.get(player);
            if (board != null) {
                board.updateLines(lines);
            }
        }
    }

    public void tick() {
        if (inDeathMatch) {
            scoreboardContribututions.clear();
            tickDeathMatch();
            return;
        }
        scoreboardContribututions.clear();
        for (Gamemode gamemode : gamemodes) {
            gamemode.tick();
        }
        scoreboardContribututions.add("§dPoints:");
        List<Player> sortedPlayerPoints = new ArrayList<>(players);
        sortedPlayerPoints.sort(Comparator.comparingInt(this::getPointsFromPlayer).reversed());
        for (Player player : sortedPlayerPoints) {
            scoreboardContribututions.add("§b" + player.getName() + ": §d" + getPointsFromPlayer(player));
        }
        displayOnBoard(scoreboardContribututions);
    }


}
