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

package world.storage;

/**
 * The interface Data accessor.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public interface DataAccessor<T, R> {
    /**
     * Load r.
     *
     * @param obj the obj
     * @return the r
     */
     R load(T obj);

    /**
     * Load r.
     *
     * @param key the key
     * @return the r
     */
     R load(String key);
}
