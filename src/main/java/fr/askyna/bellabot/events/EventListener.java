package fr.askyna.bellabot.events;

public interface EventListener<T extends Event> {
    void onEvent(T event);
}