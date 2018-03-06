package world.entity.location;

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

    public Vector subtractX(int x) {
        this.x -= x;
        return this;
    }

    public Vector subtractY(int y) {
        this.y -= y;
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

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Vector)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Vector v = (Vector) obj;

        return v.getX() == getX() && v.getY() == getY() && v.getZ() == getZ();

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

    public Vector setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Vector setY(int y) {
        this.y = y;
        return this;
    }

    public int getZ() {
        return z;
    }

    public Vector setZ(int z) {
        this.z = z;
        return this;
    }

    public Vector copy() {
        return new Vector(x, y, z);
    }
}
