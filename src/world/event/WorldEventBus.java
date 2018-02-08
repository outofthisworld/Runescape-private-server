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
        world.submit(() -> {
            List<EventHandler> handlers = getRegisteredEvents().get(event.getClass());
            handlers.forEach((h) -> {
                h.handle(event);
            });
        });
    }
}
