package world.event;

import world.event.impl.Event;

public class DefaultEventBus extends AbstractEventBus {
    @Override
    public <T extends Event> void fire(T event) {
        registeredEvents.get(event.getClass()).handle(event);
    }
}
