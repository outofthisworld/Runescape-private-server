package world.event;

import world.event.impl.Event;

import java.util.List;

public class DefaultEventBus extends AbstractEventBus {
    @Override
    public <T extends Event> void fire(T event) {
        List<EventHandler> handlers = getRegisteredEvents().get(event.getClass().isAnonymousClass()?event.getClass().getSuperclass():event.getClass());

        if (handlers == null) {
            return;
        }
        handlers.forEach((h) -> {
            h.handle(event);
        });
    }
}
