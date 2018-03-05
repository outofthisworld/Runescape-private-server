package database.transform;

public class Projectile {
    /**
     * The starting position of the projectile.
     */
    private final Position start;
    /**
     * The speed of the projectile.
     */
    private final int speed;
    /**
     * The id of the projectile.
     */
    private final int projectileId;
    /**
     * The starting height of the projectile.
     */
    private final int startHeight;
    /**
     * The ending height of the projectile.
     */
    private final int endHeight;
    /**
     * The lock on value of the projectile.
     */
    private final int lockon;
    /**
     * The delay of the projectile.
     */
    private final int delay;
    /**
     * The curve angle of the projectile.
     */
    private final int curve;
    /**
     * The offset position of the projectile.
     */
    private Position offset;

    /**
     * Creates a new {@link Projectile}.
     *
     * @param start        the starting position of the projectile.
     * @param end          the ending position of the projectile.
     * @param lockon       the lock on value of the projectile.
     * @param projectileId the id of the projectile.
     * @param speed        the speed of the projectile.
     * @param delay        the delay of the projectile.
     * @param startHeight  the starting height of the projectile.
     * @param endHeight    the ending height of the projectile.
     * @param curve        the curve angle of the projectile.
     */
    public Projectile(Position start, Position end, int lockon, int projectileId, int speed, int delay, int startHeight, int endHeight,
                      int curve) {
        this.start = start;
        this.lockon = lockon;
        this.projectileId = projectileId;
        this.delay = delay;
        this.speed = speed;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.curve = curve;
    }

    /**
     * Creates a new {@link Projectile} based on the difference between the
     * {@code source} and {@code victim}.
     *
     * @param source       the character that is firing this projectile.
     * @param victim       the victim that this projectile is being fired at.
     * @param projectileId the id of the projectile.
     * @param speed        the speed of the projectile.
     * @param delay        the delay of the projectile.
     * @param startHeight  the starting height of the projectile.
     * @param endHeight    the ending height of the projectile.
     * @param curve        the curve angle of the projectile.
     */
    public Projectile(CharacterNode source, CharacterNode victim, int projectileId, int delay, int speed, int startHeight, int endHeight,
                      int curve) {
        this(source.getPosition(), victim.getPosition(), 0, projectileId, delay, speed, startHeight, endHeight, curve);
    }

    public Position getStart() {
        return start;
    }

    public Position getOffset() {
        return offset;
    }

    public int getSpeed() {
        return speed;
    }

    public int getProjectileId() {
        return projectileId;
    }

    public int getStartHeight() {
        return startHeight;
    }

    public int getEndHeight() {
        return endHeight;
    }

    public int getLockon() {
        return lockon;
    }

    public int getDelay() {
        return delay;
    }

    public int getCurve() {
        return curve;
    }
}
