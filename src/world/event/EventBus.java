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

/**
 * The interface AbstractEvent bus.
 */
public interface EventBus {
    /**
     * Register.
     *
     * @param <T>     the type parameter
     * @param klazz   the klazz
     * @param handler the handler
     */
    <T extends AbstractEvent> void register(Class<T> klazz, EventHandler<? super T> handler);

    /**
     * Fire.
     *
     * @param <T>   the type parameter
     * @param event the event
     */
    <T extends AbstractEvent> void fire(T event);

    /**
     * Register.
     *
     * @param obj the obj
     */
    void register(Object obj);
}
