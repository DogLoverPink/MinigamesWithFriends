package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.configs.GameModuleConfig;
import doglover.minigameswithfriends.events.EventSubscriber;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class GameModule extends EventSubscriber {

    public enum Type {
        GAMEMODE,
        MODIFIER
    }

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

    public abstract GameModuleConfig getConfig();

    public abstract Type getType();

    public record Registration(String name, Class<? extends GameModule> moduleClass,
                               Supplier<? extends GameModule> factory, GameModuleConfig config, Type type) {
    }

    private static final Map<String, Registration> REGISTRY = new LinkedHashMap<>();

    static void registerModule(String name, Class<? extends GameModule> moduleClass,
                               Supplier<? extends GameModule> factory, GameModuleConfig config, Type type) {
        REGISTRY.put(name.toLowerCase(), new Registration(name, moduleClass, factory, config, type));
    }

    private static Registration lookup(String name) {
        return name == null ? null : REGISTRY.get(name.toLowerCase());
    }

    public static boolean isValidModule(String name) {
        return lookup(name) != null;
    }

    public static boolean isValidModule(String name, Type type) {
        Registration registration = lookup(name);
        return registration != null && registration.type() == type;
    }

    public static GameModule getModuleFromName(String name) {
        Registration registration = lookup(name);
        return registration == null ? null : registration.factory().get();
    }

    public static GameModuleConfig getConfigFromName(String name) {
        Registration registration = lookup(name);
        return registration == null ? null : registration.config();
    }

    public static List<String> getModuleList() {
        List<String> names = new ArrayList<>();
        for (Registration registration : REGISTRY.values()) {
            names.add(registration.name());
        }
        return names;
    }

    public static List<String> getModuleList(Type type) {
        List<String> names = new ArrayList<>();
        for (Registration registration : REGISTRY.values()) {
            if (registration.type() == type) {
                names.add(registration.name());
            }
        }
        return names;
    }

    public static String getModuleListString(Type type) {
        return String.join(", ", getModuleList(type));
    }

    public static List<Class<? extends GameModule>> getModuleClassList(Type type) {
        List<Class<? extends GameModule>> classes = new ArrayList<>();
        for (Registration registration : REGISTRY.values()) {
            if (registration.type() == type) {
                classes.add(registration.moduleClass());
            }
        }
        return classes;
    }

    public static String getModuleNameFromClass(Class<? extends GameModule> moduleClass) {
        if (moduleClass == null) {
            return "";
        }
        for (Registration registration : REGISTRY.values()) {
            if (registration.moduleClass() == moduleClass) {
                return registration.name();
            }
        }
        return moduleClass.getSimpleName().replace("Gamemode", "").replace("Modifier", "");
    }
}
