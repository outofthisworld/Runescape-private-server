package world.entity.player.combat.magic;

/**
 * The type Projectile.
 */
public class Projectile {
    private int id;
    private int startHeight;
    private int endHeight;
    private int curve;
    private int speed;
    private int delay;

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets start height.
     *
     * @return the start height
     */
    public int getStartHeight() {
        return startHeight;
    }

    /**
     * Gets end height.
     *
     * @return the end height
     */
    public int getEndHeight() {
        return endHeight;
    }

    /**
     * Gets curve.
     *
     * @return the curve
     */
    public int getCurve() {
        return curve;
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets delay.
     *
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }
}
