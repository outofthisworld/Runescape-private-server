package world.area;

import util.random.RandomUtils;

public class Area {

    public static abstract class Location {
        public abstract boolean contains(int x, int y);

        public abstract Position generateRandomPosition();
    }

    public static class TwoDimensional {

        public static class Polygon extends Location {
            @Override
            public boolean contains(int x, int y) {
                return false;
            }

            @Override
            public Position generateRandomPosition() {
                return null;
            }
        }

        public static class Circle extends Location {

            private int centerX;
            private int centerY;
            private int radius;

            public Circle(int cX, int cY, int radius) {
                this.centerX = cX;
                this.centerY = cY;
                this.radius = radius;
            }

            public boolean contains(int x, int y) {
                int dx = centerX - x;
                int dy = centerY - y;

                return Math.pow(dx, 2) + Math.pow(dy, 2) <= Math.pow(radius, 2);
            }

            public int getCenterX() {
                return centerX;
            }

            public void setCenterX(int centerX) {
                this.centerX = centerX;
            }

            public int getCenterY() {
                return centerY;
            }

            public void setCenterY(int centerY) {
                this.centerY = centerY;
            }

            public int getRadius() {
                return radius;
            }

            public void setRadius(int radius) {
                this.radius = radius;
            }

            /**
             * @return
             */
            public Position generateRandomPosition() {
                double rad = Math.toRadians(RandomUtils.randomIntBetween(0, 360));
                int x = centerX + (int) (Math.cos(rad) * radius);
                int y = centerY + (int) (Math.sin(rad) * radius);
                return new Position(RandomUtils.randomIntBetween(centerX, centerX + Math.abs(centerX - x)),
                        RandomUtils.randomIntBetween(centerY, centerY + Math.abs(centerY - y)), 0);
            }
        }


        public static class Rectangle extends Location {
            private int topLeftX;
            private int topLeftY;
            private int bottomLeftX;
            private int bottomLeftY;
            private int topRightX;
            private int topRightY;
            private int bottomRightX;
            private int bottomRightY;

            public Rectangle(int topLeftX, int topLeftY, int width, int height) {
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
             * Constructs a square area with an equal perimeter around the given x,y coords.
             */
            public Rectangle(int x, int y, int perimeter) {
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
            @Override
            public Position generateRandomPosition() {
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
    }

    public static class ThreeDimensional {
        public static class Cuboid extends TwoDimensional.Rectangle {

            public Cuboid(int topLeftX, int topLeftY, int width, int height) {
                super(topLeftX, topLeftY, width, height);
            }
        }

        public static class Cylinder extends TwoDimensional.Circle {

            public Cylinder(int cX, int cY, int radius) {
                super(cX, cY, radius);
            }
        }

        public static class Polyhedron extends TwoDimensional.Polygon {

        }
    }
}
