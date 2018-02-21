package world.entity.update;

/**
 * The interface Flag.
 *
 * @param <T> the type parameter
 */
public interface IFlag<T> {
    /**
     * Sets flag.
     *
     * @param flag the flag
     */
    void setFlag(T flag);

    /**
     * Toggle flag.
     *
     * @param flag the flag
     */
    void toggleFlag(T flag);

    /**
     * Unset flag.
     *
     * @param flag the flag
     */
    void unsetFlag(T flag);

    /**
     * Is set boolean.
     *
     * @param flag the flag
     * @return the boolean
     */
    boolean isSet(T flag);

    /**
     * Any set boolean.
     *
     * @return the boolean
     */
    boolean anySet();

    /**
     * Clear.
     */
    void clear();


    /**
     * Gets mask.
     *
     * @return the mask
     */
    long getMask();
}
