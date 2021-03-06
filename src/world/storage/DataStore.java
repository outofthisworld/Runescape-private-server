package world.storage;

/**
 * The interface Data store.
 *
 * @param <T> the type parameter
 * @param <U> the type parameter
 */
public interface DataStore<T, U> {

    /**
     * Store t.
     *
     * @param o the o
     * @return the t
     */
    T store(String key, U o);
}
