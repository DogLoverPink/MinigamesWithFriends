package doglover.minigameswithfriends.wouldyourather;

import doglover.minigameswithfriends.wouldyourather.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WYREventHandler {
    private static final Map<Class<? extends Event>, List<WYREffectWithPriority>> eventMappings = new HashMap<>();

    public static List<WYREffect> getEffectsForEvent(Event event) {
        return eventMappings.getOrDefault(event.getClass(), new ArrayList<>()).stream().map(WYREffectWithPriority::getEffect).toList();
    }

    public static void registerEvents(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new WYRDeathEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRInteractionEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRPlayerMovementEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRDamageEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRBlockEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRItemEvents(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WYRInventoryEvents(), plugin);
    }

    public static void subscribe(Class<? extends Event> eventClass, WYREffect effect, EventPriority priority) {
        eventMappings.putIfAbsent(eventClass, new ArrayList<>());
        addEventClassInCorrectOrder(eventClass, new WYREffectWithPriority(effect, priority));
    }

    private static void addEventClassInCorrectOrder(Class<? extends Event> eventClass, WYREffectWithPriority effectWithPriority) {
        List<WYREffectWithPriority> effects = eventMappings.computeIfAbsent(eventClass, k -> new ArrayList<>());
        int index = 0;
        while (index < effects.size() && effects.get(index).getPriority().ordinal() >= effectWithPriority.getPriority().ordinal()) {
            index++;
        }
        effects.add(index, effectWithPriority);
    }

    public static void subscribe(Class<? extends Event> eventClass, WYREffect effect) {
        subscribe(eventClass, effect, EventPriority.NORMAL);
    }



    public static void unsubscribe(WYREffect effect) {
        for (List<WYREffectWithPriority> effects : eventMappings.values()) {
            effects.removeIf(e -> e.getEffect().equals(effect));
        }
    }

    private static boolean isActive = false;

    public static void setActive(boolean isActive) {
        WYREventHandler.isActive = isActive;
    }

    public static boolean isActive() {
        return isActive;
    }

    public static void unsubscribe(Class<? extends Event> eventClass, WYREffect effect) {
        eventMappings.get(eventClass).removeIf(e -> e.getEffect().equals(effect));
    }

    static class WYREffectWithPriority {
        private final EventPriority priority;
        private final WYREffect effect;

        public WYREffectWithPriority(WYREffect effect, EventPriority priority) {
            this.priority = priority;
            this.effect = effect;
        }

        public EventPriority getPriority() {
            return priority;
        }

        public WYREffect getEffect() {
            return effect;
        }
    }

}
