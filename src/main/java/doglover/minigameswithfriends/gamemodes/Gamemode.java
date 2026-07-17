package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.configs.GamemodeConfig;
import doglover.minigameswithfriends.events.EventSubscriber;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class Gamemode extends EventSubscriber {

    public abstract void tick();

    public abstract void onGameEnd();

    public abstract void onGameStart();

    public void onPlayerJoin(Player plr) {

    }

    public  void onPlayerLeave(Player plr) {

    }

    public void onDeathMatchEnd() {

    }

    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public abstract void updateConfig();

    public abstract GamemodeConfig getConfig();

    public record Registration(String name, Class<? extends Gamemode> type,
                               Supplier<Gamemode> factory, GamemodeConfig config) {
    }

    private static final Map<String, Registration> REGISTRY = new LinkedHashMap<>();

    public static void register(String name, Class<? extends Gamemode> type,
                                Supplier<Gamemode> factory, GamemodeConfig config) {
        REGISTRY.put(name.toLowerCase(), new Registration(name, type, factory, config));
    }

    private static Registration lookup(String name) {
        return name == null ? null : REGISTRY.get(name.toLowerCase());
    }

    public static boolean isValidGamemode(String gamemodeName) {
        return lookup(gamemodeName) != null;
    }

    public static Gamemode getGamemodeFromName(String gamemodeName) {
        Registration registration = lookup(gamemodeName);
        return registration == null ? null : registration.factory().get();
    }

    public static GamemodeConfig getConfigFromName(String gamemodeName) {
        Registration registration = lookup(gamemodeName);
        return registration == null ? null : registration.config();
    }

    public static List<String> getGamemodeList() {
        return REGISTRY.values().stream().map(Registration::name).toList();
    }

    public static String getGamemodeListString() {
        return String.join(", ", getGamemodeList());
    }

    public static List<Class<? extends Gamemode>> getGamemodeClassList() {
        List<Class<? extends Gamemode>> classes = new ArrayList<>();
        for (Registration registration : REGISTRY.values()) {
            classes.add(registration.type());
        }
        return classes;
    }

    public static String getGamemodeNameFromClass(Class<? extends Gamemode> gamemodeClass) {
        if (gamemodeClass == null) {
            return "";
        }
        for (Registration registration : REGISTRY.values()) {
            if (registration.type() == gamemodeClass) {
                return registration.name();
            }
        }
        return gamemodeClass.getSimpleName().replace("Gamemode", "");
    }
}
