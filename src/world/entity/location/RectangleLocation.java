package world.entity.location;

import util.random.RandomUtils;

public class RectangleLocation {
    private int topLeftX;
    private int topLeftY;
    private int bottomLeftX;
    private int bottomLeftY;
    private int topRightX;
    private int topRightY;
    private int bottomRightX;
    private int bottomRightY;

    public RectangleLocation(int topLeftX, int topLeftY, int width, int height) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.topRightX = this.topLeftX + width;
        this.topRightY = topLeftY;
        this.bottomLeftX = topLeftX;
        this.bottomLeftY = topLeftY + height;
        this.bottomRightX = this.topRightX;
        this.bottomRightY = this.bottomLeftY;
    }

    /**
     * Constructs a square location with an equal perimeter around the given x,y coords.
     */
    public RectangleLocation(int x, int y, int perimeter) {
        this(x - (perimeter / 2), y - (perimeter / 2), perimeter, perimeter);
    }

    public boolean contains(int x, int y) {
        return x >= topLeftX && x <= topRightX && y >= topLeftY && y <= topRightY;
    }

    public boolean contains(Position p) {
        return contains(p.getVector().getX(), p.getVector().getY());
    }

    public boolean contains(Vector v) {
        return contains(v.getX(), v.getY());
    }

    /**
     * Generate a random point within this locations bounds.
     *
     * @return
     */
    public Position getRandomPositon() {
        return new Position(RandomUtils.randomIntBetween(topLeftX, topRightX), RandomUtils.randomIntBetween(topLeftY, bottomLeftY), 0);
    }

    public int getWidth() {
        return topRightX - topLeftX;
    }

    public int getHeight() {
        return bottomLeftY - topLeftY;
    }

    public int getArea() {
        return getWidth() * getHeight();
    }

    public int getTopLeftX() {
        return topLeftX;
    }

    public int getTopLeftY() {
        return topLeftY;
    }

    public int getBottomLeftX() {
        return bottomLeftX;
    }

    public int getBottomLeftY() {
        return bottomLeftY;
    }

    public int getTopRightX() {
        return topRightX;
    }

    public int getTopRightY() {
        return topRightY;
    }

    public int getBottomRightX() {
        return bottomRightX;
    }

    public int getBottomRightY() {
        return bottomRightY;
    }
}
