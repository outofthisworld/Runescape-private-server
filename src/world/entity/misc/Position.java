package world.entity.misc;

/**
 * The type Position.
 */
public class Position {
    private final Vector v;

    /**
     * Instantiates a new Position.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public Position(int x, int y, int z) {
        v = new Vector(x, y, z);
    }

    /**
     * Instantiates a new Position.
     *
     * @param v the v
     */
    public Position(Vector v) {
        this.v = v;
    }

    /**
     * Gets vector.
     *
     * @return the vector
     */
    public Vector getVector() {
        return v;
    }

    /**
     * Pythagorean distance to int.
     *
     * @param other the other
     * @return the int
     */
    public int pythagoreanDistanceTo(Position other) {
        return v.copy().subtract(other.getVector()).length();
    }

    /**
     * Manhattan distance to int.
     *
     * @param other the other
     * @return the int
     */
    public int manhattanDistanceTo(Position other) {
        Vector deltaVec = v.copy().subtract(other.getVector());
        return Math.abs(deltaVec.getX() + deltaVec.getY() + deltaVec.getZ());
    }

    public boolean isWithinX(Position other, int x) {
        return Math.abs(distanceBetweenX(other)) <= x;
    }

    public boolean isWithinY(Position other, int y) {
        return Math.abs(distanceBetweenX(other)) <= y;
    }

    public boolean isWithinZ(Position other, int z) {
        return Math.abs(distanceBetweenZ(other)) <= z;
    }

    public boolean isWithinXY(Position other, int check) {
        return isWithinX(other, check) && isWithinY(other, check);
    }

    public int distanceBetweenX(Position other) {
        return v.copy().subtract(other.getVector()).getX();
    }

    public int distanceBetweenY(Position other) {
        return v.copy().subtract(other.getVector()).getY();
    }

    public int distanceBetweenZ(Position other) {
        return v.copy().subtract(other.getVector()).getZ();
    }
}
