package world.entity.update;

/**
 * The type Update flags.
 */
public class Flags implements IFlag<Long> {

    /**
     * The Mask.
     */
    protected long mask;


    public Flags() {
        mask = 0;
    }

    /**
     * Sets flag.
     *
     * @param flag the flag
     */
    @Override
    public void setFlag(Long flag) {
        mask |= flag.longValue();
    }

    /**
     * Toggle flag.
     *
     * @param flag the flag
     */
    @Override
    public void toggleFlag(Long flag) {
        mask ^= flag.longValue();
    }

    /**
     * Unset flag.
     *
     * @param flag the flag
     */
    @Override
    public void unsetFlag(Long flag) {
        mask &= (~flag.longValue());
    }

    /**
     * Is set boolean.
     *
     * @param flag the flag
     * @return the boolean
     */
    @Override
    public boolean isSet(Long flag) {
        return (mask & flag) == flag.longValue();
    }

    /**
     * Any set boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean anySet() {
        return mask > 0;
    }

    /**
     * Clear.
     */
    @Override
    public void clear() {
        mask = 0;
    }

    /**
     * Gets mask.
     *
     * @return the mask
     */
    @Override
    public long getMask() {
        return mask;
    }
}
