package fr.askyna.bellabot.events;

import fr.askyna.bellabot.BellaBot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {
    private final Map<Class<? extends Event>, Set<EventListener<? extends Event>>> listeners = new HashMap<>();

    public <T extends Event> void registerEvent(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(listener);
    }

    public <T extends Event> void unregisterEvent(Class<T> eventType, EventListener<T> listener) {
        Set<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    public <T extends Event> void triggerEvent(T event) {
        Set<EventListener<? extends Event>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener<? extends Event> listener : eventListeners) {
                //noinspection unchecked
                ((EventListener<T>) listener).onEvent(event);
            }
        }
    }

    // Méthode pour enregistrer les listeners JDA également
    public void registerJDAListerners(ListenerAdapter listener) {
        BellaBot.getJDA().addEventListener(listener);
    }

    public void unregisterJDAListerners(ListenerAdapter listener) {
        BellaBot.getJDA().removeEventListener(listener);
    }
}