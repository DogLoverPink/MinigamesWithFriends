package doglover.minigameswithfriends.wouldyourather;

import doglover.minigameswithfriends.events.DeathListener;
import doglover.minigameswithfriends.wouldyourather.events.WYRDeathEvents;
import doglover.minigameswithfriends.wouldyourather.events.WYRInteractionEvents;
import doglover.minigameswithfriends.wouldyourather.events.WYRPlayerSneakEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WYREventHandler {
    private static Map<Class<? extends Event>, List<WYREffect>> eventMappings = new HashMap<>();

    public static List<WYREffect> getEffectsForEvent(Event event) {
        return eventMappings.get(event.getClass());
    }

    public static void registerEvents(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new WYRDeathEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRInteractionEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRPlayerSneakEvent(), plugin);
    }

    public static void subscribe(Class<? extends Event> eventClass, WYREffect effect) {
        eventMappings.putIfAbsent(eventClass, new ArrayList<>());
        eventMappings.get(eventClass).add(effect);
    }

    public static void unsubscribe(WYREffect effect) {
        for (List<WYREffect> effects : eventMappings.values()) {
            effects.remove(effect);
        }
    }

    private static boolean isActive = false;

    public static boolean isActive() {
        return isActive;
    }

    public static void unsubscribe(Class<? extends Event> eventClass, WYREffect effect) {
        eventMappings.get(eventClass).remove(effect);
    }
}
