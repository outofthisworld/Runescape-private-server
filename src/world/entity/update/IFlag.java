package world.entity.update;

public interface IFlag<T> {
    /**
     * Sets flag.
     *
     * @param flag the flag
     */
    public void setFlag(T flag);

    /**
     * Toggle flag.
     *
     * @param flag the flag
     */
    public void toggleFlag(T flag);

    /**
     * Unset flag.
     *
     * @param flag the flag
     */
    public void unsetFlag(T flag);

    /**
     * Is set boolean.
     *
     * @param flag the flag
     * @return the boolean
     */
    public boolean isSet(T flag);

    /**
     * Any set boolean.
     *
     * @return the boolean
     */
    public boolean anySet();

    /**
     * Clear.
     */
    public void clear();

    /**
     * Gets mask.
     *
     * @return the mask
     */
    long getMask();
}
