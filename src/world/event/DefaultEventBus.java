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

import world.event.impl.AbstractEvent;

import java.util.List;

/**
 * The type Default event bus.
 */
public class DefaultEventBus extends AbstractEventBus {
    @Override
    public <T extends AbstractEvent> void fire(T event) {
        List<EventHandler> handlers = getRegisteredEvents().get(event.getClass().isAnonymousClass() ? event.getClass().getSuperclass() : event.getClass());

        if (handlers == null) {
            return;
        }
        handlers.forEach((h) -> {
            h.handle(event);
        });
    }
}
