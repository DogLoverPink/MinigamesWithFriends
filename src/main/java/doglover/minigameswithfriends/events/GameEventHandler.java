package doglover.minigameswithfriends.events;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventHandler {

    private static final Map<Class<? extends Event>, List<SubscriberWithPriority>> eventMappings = new HashMap<>();

    public static List<EventSubscriber> getSubscribersForEvent(Event event) {
        return eventMappings.getOrDefault(event.getClass(), new ArrayList<>())
                .stream().map(SubscriberWithPriority::subscriber).toList();
    }

    public static void registerListeners(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new GameEventListener(), plugin);
    }

    public static void subscribe(Class<? extends Event> eventClass, EventSubscriber subscriber, EventPriority priority) {
        addSubscriberInCorrectOrder(eventClass, new SubscriberWithPriority(subscriber, priority));
    }

    public static void subscribe(Class<? extends Event> eventClass, EventSubscriber subscriber) {
        subscribe(eventClass, subscriber, EventPriority.NORMAL);
    }


    private static void addSubscriberInCorrectOrder(Class<? extends Event> eventClass, SubscriberWithPriority subscriberWithPriority) {
        List<SubscriberWithPriority> subscribers = eventMappings.computeIfAbsent(eventClass, k -> new ArrayList<>());
        int index = 0;
        while (index < subscribers.size() && subscribers.get(index).priority().ordinal() <= subscriberWithPriority.priority().ordinal()) {
            index++;
        }
        subscribers.add(index, subscriberWithPriority);
    }

    public static void unsubscribe(EventSubscriber subscriber) {
        for (List<SubscriberWithPriority> subscribers : eventMappings.values()) {
            subscribers.removeIf(s -> s.subscriber().equals(subscriber));
        }
    }

    public static void unsubscribe(Class<? extends Event> eventClass, EventSubscriber subscriber) {
        List<SubscriberWithPriority> subscribers = eventMappings.get(eventClass);
        if (subscribers != null) {
            subscribers.removeIf(s -> s.subscriber().equals(subscriber));
        }
    }

    public static boolean isDispatchActive() {
        Game game = MinigamesWithFriends.getGame();
        return game != null && game.isRunning() && !game.isPaused();
    }

    record SubscriberWithPriority(EventSubscriber subscriber, EventPriority priority) {
    }
}
