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

/**
 * The interface Event handler.
 *
 * @param <T> the type parameter
 */
public interface EventHandler<T> {
    /**
     * Handle.
     *
     * @param event the event
     */
    void handle(T event);
}