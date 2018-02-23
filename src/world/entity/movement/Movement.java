package world.entity.movement;

import world.entity.Entity;
import world.entity.misc.Position;
import world.entity.player.Player;

import java.util.Deque;
import java.util.LinkedList;

public class Movement {

    public static final int NORTH_WEST = 0;
    public static final int NORTH = 1;
    public static final int NORTH_EAST = 2;
    public static final int WEST = 3;
    public static final int EAST = 4;
    public static final int SOUTH_WEST = 5;
    public static final int SOUTH = 6;
    public static final int SOUTH_EAST = 7;
    private final Entity e;
    private final boolean isRunning = false;
    private final Deque<Position> movementQueue = new LinkedList<>();
    private Position lastPosition = null;
    private Position lastRegion = null;
    private int direction = -1;

    public Movement(Entity e) {
        this.e = e;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public void poll() {
        Position movementPosition = movementQueue.poll();

        if (movementPosition == null) {
            resetMovement();
            return;
        }

        System.out.println("Movement position x: " + movementPosition.getVector());

        direction = parseDirection(movementPosition.getVector().getX() - lastPosition.getVector().getX(),
                movementPosition.getVector().getY() - lastPosition.getVector().getY());


        e.getPosition().setVector(movementPosition.getVector());

        if (e.isPlayer()) {
            Player player = (Player) e;

            int deltaX = player.getPosition().getMapOffsetX();

            int deltaY = player.getPosition().getMapOffsetY();

            System.out.println(deltaX);
            System.out.println(deltaY);

           /* Position regionPosition = player.getPosition().getRegionPosition();
            if (deltaX <= 16 || deltaX >= 88 || deltaY <= 16 || deltaY >= 88) {
                System.out.println("firing region changed");
                lastRegion = regionPosition;
                player.send(new RegionUpdateEvent(player, this));
            }*/

           /*if(!e.getPosition().equals(player.getLastRegionPosition())){
               player.send(new RegionUpdateEvent(player, this));
           }*/

            //Send movement event
            /// player.send(new PlayerMoveEvent(player, this));
        }


        lastPosition = movementPosition;

    }

    public boolean isMoving() {
        return direction != -1;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getDirection() {
        return direction;
    }

    public void resetMovement() {
        lastPosition = null;
        lastRegion = null;
        movementQueue.clear();
        direction = -1;
    }

    public void beginMovement() {
        if (movementQueue.size() > 0) {
            resetMovement();
        }
        movementQueue.addFirst(e.getPosition());
    }

    public void finishMovement() {
        if (movementQueue.size() == 0) {
            throw new IllegalStateException("Movement has not begun");
        }
        lastPosition = movementQueue.poll();
        lastRegion = e.getPosition().getRegionPosition();
    }

    public void stepTo(int x, int y) {

        Position entityPosition = movementQueue.peekLast();

        if (entityPosition == null) {
            throw new IllegalStateException("Movement has not begun");
        }

        int dx = x - entityPosition.getVector().getX();
        int dy = y - entityPosition.getVector().getY();
        System.out.println("dx : " + dx);
        System.out.println("dy: " + dy);

        int max = Math.max(Math.abs(dx), Math.abs(dy));


        for (int i = 0; i < max; i++) {

            if (dx > 0) {
                dx--;
            } else if (dx < 0) {
                dx++;
            }

            if (dy > 0) {
                dy--;
            } else if (dy < 0) {
                dy++;
            }
            movementQueue.addLast(new Position(x - dx, y - dy, 0));
        }

        System.out.println("Built movement queue:");
        movementQueue.forEach(e -> {
            System.out.println("x: " + e.getVector().getX() + " y: " + e.getVector().getY());
        });
    }

    private int parseDirection(int deltaX, int deltaY) {
        //Delta x is negative they are stepping west
        if (deltaX < 0) {


            if (deltaY < 0) {
                return SOUTH_WEST; //south west
            }

            if (deltaY > 0) {
                return NORTH_WEST; // north west
            }

            //They moved east/right one place.
            return Movement.WEST;
        }

        //Delta x is positive they are stepping east
        if (deltaX > 0) {

            if (deltaY < 0) {
                return SOUTH_EAST; //south east
            }

            if (deltaY > 0) {
                return NORTH_EAST; //North east
            }
            return Movement.EAST;
        }

        if (deltaY < 0) {
            return SOUTH; // south
        }

        if (deltaY > 0) {
            return NORTH; // north
        }

        return -1;
    }

}
