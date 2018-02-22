/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package world.event;

import world.World;
import world.event.impl.Event;

import java.util.List;
import java.util.logging.Logger;

/**
 * The type World event bus.
 */
public class WorldEventBus extends AbstractEventBus {
    private static final Logger logger = Logger.getLogger(WorldEventBus.class.getName());
    private final World world;

    /**
     * Instantiates a new World event bus.
     *
     * @param world the world
     */
    public WorldEventBus(World world) {
        this.world = world;
    }

    @Override
    public <T extends Event> void fire(T event) {
        Class<?> c = event.getClass().isAnonymousClass() ? event.getClass().getSuperclass() : event.getClass();
        List<EventHandler> handlers = getRegisteredEvents().get(c);

        if (handlers.size() == 0) {
            return;
        }

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
