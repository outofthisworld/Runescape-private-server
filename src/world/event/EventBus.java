package world.event;


import world.event.impl.Event;

import java.util.HashMap;

public class EventBus {

    private final HashMap<Class<?>, EventHandler> hm = new HashMap<>();

    public <T extends Event> void register(Class<T> klazz, EventHandler<? super T> handler) {
        hm.put(klazz, handler);
    }

    public <T extends Event> void fire(T event) {
        hm.get(event.getClass()).handle(event);
    }
}
