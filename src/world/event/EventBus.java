package world.event;

import world.event.impl.Event;

public interface EventBus {
    <T extends world.event.impl.Event> void register(Class<T> klazz, EventHandler<? super T> handler);

    <T extends Event> void fire(T event);

    void register(Object obj);
}
