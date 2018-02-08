package world.event;

import world.World;
import world.event.impl.Event;

import java.util.List;

public class WorldEventBus extends AbstractEventBus {
    private final World world;

    public WorldEventBus(World world) {
        this.world = world;
    }

    @Override
    public <T extends Event> void fire(T event) {
        List<EventHandler> handlers = getRegisteredEvents().get(event.getClass().isAnonymousClass() ? event.getClass().getSuperclass() : event.getClass());

        world.submit(() -> {
            if (handlers == null) {
                return;
            }
            handlers.forEach((h) -> {
                h.handle(event);
            });
        });
    }
}
