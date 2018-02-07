package world.event;

public interface EventHandler<T> {
    void handle(T event);
}