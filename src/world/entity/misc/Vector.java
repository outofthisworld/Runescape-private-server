package world.entity.misc;

public class Vector {

    private int x;
    private int y;
    private int z;

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector add(Vector other) {
        return addX(other.x).addY(other.y);
    }

    public Vector addX(int x) {
        this.x += x;
        return this;
    }

    public Vector addY(int y) {
        this.y += y;
        return this;
    }

    public Vector subtract(Vector other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public int length() {
        return (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public int dot(Vector other) {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public Vector setLength(int length) {
        int currentAngle = getAngle();
        x = (int) (Math.cos(currentAngle) * length);
        y = (int) (Math.sin(currentAngle) * length);
        return this;
    }

    public int getAngle() {
        return (int) Math.atan2(y, x);
    }

    public Vector toUnitVector() {
        x /= length();
        y /= length();
        z /= length();
        return this;
    }

    public Vector multiply(int value) {
        return multiplyX(value).multiplyY(value);
    }

    public Vector multiplyX(int value) {
        x *= value;
        return this;
    }

    public Vector multiplyY(int value) {
        y *= value;
        return this;
    }

    public Vector multiply(Vector other) {
        return multiplyX(other.x).multiplyY(other.y);
    }

    public Vector divide(Vector other) {
        x /= other.x;
        y /= other.y;
        return this;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Vector copy() {
        return new Vector(x, y, z);
    }
}
