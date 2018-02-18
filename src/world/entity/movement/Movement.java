package world.entity.movement;

import world.entity.Entity;
import world.entity.misc.Position;
import world.entity.player.Player;
import world.event.impl.PlayerMoveEvent;
import world.event.impl.RegionUpdateEvent;

import java.util.Deque;
import java.util.LinkedList;

public class Movement {

    private static final int EAST = 3;
    private static final int WEST = 4;
    private final Entity e;
    private final boolean isRunning = false;
    private final Deque<Position> movementQueue = new LinkedList<>();
    private Position lastPosition = null;
    private int direction = -1;

    public Movement(Entity e) {
        this.e = e;
    }

    public void poll() {
        Position movementPosition = movementQueue.poll();

        if (movementPosition == null) {
            return;
        }

        if (movementQueue.size() == 0) {
            direction = -1;
        }

        direction = parseDirection(movementPosition.getVector().getX() - lastPosition.getVector().getX(),
                movementPosition.getVector().getY() - lastPosition.getVector().getY());

        lastPosition = movementPosition;

        e.getPosition().getVector().add(movementPosition.getVector());

        if (e.isPlayer()) {
            Player player = (Player) e;


            int mapOffsetX = player.getPosition().getMapOffsetX();
            int mapOffsetY = player.getPosition().getMapOffsetY();

            if (mapOffsetX <= 16 || mapOffsetX >= 88 || mapOffsetY <= 16 || mapOffsetY >= 88) {
                e.getWorld().getEventBus().fire(new RegionUpdateEvent(player, this));
            }

            //Send movement event
            e.getWorld().getEventBus().fire(new PlayerMoveEvent(player, this));
        }
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
        movementQueue.clear();
        direction = -1;
    }

    public void stepTo(int x, int y) {
        resetMovement();

        Position entityPosition = e.getPosition();

        if (entityPosition.getVector().getX() == x || entityPosition.getVector().getY() == y) {
            return;
        }

        lastPosition = entityPosition;

        int max = Math.max(x, y);

        movementQueue.addFirst(new Position(x, y, -1));

        for (int i = 0; i < max; i++) {
            if (x > entityPosition.getVector().getX()) {
                x--;
            } else if (x < entityPosition.getVector().getX()) {
                x++;
            }

            if (y > entityPosition.getVector().getY()) {
                y++;
            } else {
                y--;
            }

            movementQueue.addFirst(new Position(x, y, 0));
        }
    }


    private int parseDirection(int deltaX, int deltaY) {
        //Delta x is negative they are stepping east/right
        if (deltaX < 0) {


            if (deltaY < 0) {
                return 5;
            }

            if (deltaY > 0) {
                return 0;
            }

            //They moved east/right one place.
            return Movement.EAST;
        }

        //Delta x is positive they are stepping left/west
        if (deltaX > 0) {

            if (deltaY < 0) {
                return 7;
            }

            if (deltaY > 0) {
                return 2;
            }

            return Movement.WEST;
        }

        if (deltaY < 0) {
            return 6;
        }
        if (deltaY > 0) {
            return 1;
        }
        return -1;
    }

}
