package world.entity.location;

import util.random.RandomUtils;

public class CircleLocation {

    private int centerX;
    private int centerY;
    private int radius;

    public CircleLocation(int cX, int cY, int radius) {
        this.centerX = cX;
        this.centerY = cY;
        this.radius = radius;
    }


    public boolean contains(int x, int y){
        int dx = centerX - x;
        int dy = centerY - y;

        return Math.pow(dx,2) + Math.pow(dy,2) <= Math.pow(radius,2);
    }


    /**
     *
     * @return
     */
    public Position generateRandomPosition(){
        double rad = Math.toRadians(RandomUtils.randomIntBetween(0,360));
        int x  = centerX + (int) (Math.cos(rad) * radius);
        int y = centerY + (int) (Math.sin(rad) * radius);
        return new Position(RandomUtils.randomIntBetween(centerX,centerX+Math.abs(centerX-x)),
                RandomUtils.randomIntBetween(centerY,centerY+Math.abs(centerY-y)),0);
    }
}
