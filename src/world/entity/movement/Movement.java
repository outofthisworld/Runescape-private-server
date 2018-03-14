package world.entity.movement;

import world.WorldConfig;
import world.entity.Entity;
import world.area.Position;
import world.entity.npc.Npc;
import world.entity.player.Player;
import world.entity.player.Skill;
import world.task.SingleExecutionTask;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

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
    private final Deque<Position> movementQueue = new LinkedList<>();
    private boolean isRunning = false;
    private Position lastPosition = null;
    private int walkDirection = -1;
    private int runDirection = -1;
    private double runEnergy = 100;
    private Consumer<Player> destinationListener = null;

    public Movement(Entity e) {
        this.e = e;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public int getRunDirection() {
        return runDirection;
    }

    public int getRunEnergy() {
        return (int) runEnergy;
    }

    public void poll() {
        Position movementPosition = movementQueue.poll();

        if (movementPosition == null) {
            if(e.isPlayer() && destinationListener != null){
                e.getWorld().queueWorldTask(new SingleExecutionTask(()->{
                    destinationListener.accept((Player) e);
                    destinationListener = null;
                }));
            }
            resetMovement();
            return;
        }

        walkDirection = parseDirection(movementPosition.getVector().getX() - lastPosition.getVector().getX(),
                movementPosition.getVector().getY() - lastPosition.getVector().getY());
        lastPosition = movementPosition;


        Position runPosition = null;

        if (isRunning) {
            runPosition = movementQueue.poll();
        }

        if (runPosition != null) {
            runDirection = parseDirection(runPosition.getVector().getX() - movementPosition.getVector().getX(),
                    runPosition.getVector().getY() - movementPosition.getVector().getY());
            lastPosition = runPosition;
            decreaseRunEnergy();
        } else {
            runDirection = -1;
            increaseRunEnergy();
        }

        e.getPosition().setVector(runPosition == null ? movementPosition.getVector().copy() : runPosition.getVector().copy());

        if (e.isPlayer()) {
            Player player = (Player) e;
            e.getWorld().getPlayerRegionDivision().updateEntityRegion(player);
            int deltaX = player.getPosition().getMapOffsetX();

            int deltaY = player.getPosition().getMapOffsetY();


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
        }else{
            e.getWorld().getNpcRegionDivision().updateEntityRegion((Npc)e);
        }
    }

    public void setDestinationListener(Consumer<Player> destinationListener){
        this.destinationListener = destinationListener;
    }

    private void decreaseRunEnergy() {
        if (e.isPlayer() && runEnergy > 0) {
            Player p = (Player) e;
            runEnergy -= calculateDecreaseRunEnergy(e.getWeight());
            runEnergy = Math.max(0, runEnergy);
            p.getClient().getOutgoingPacketBuilder()
                    .setRunEnergy((int) runEnergy)
                    .sendMessage("Your run energy is now: " + (int) runEnergy)
                    .send();

        }
    }

    private void increaseRunEnergy() {
        if (e.isPlayer() && runEnergy < 100) {
            Player p = (Player) e;
            runEnergy += calculateIncreaseRunEnergy(p.getSkills().getSkillLevel(Skill.AGILITY));
            runEnergy = Math.min(100, runEnergy);
            p.getClient().getOutgoingPacketBuilder().setRunEnergy((int) runEnergy);
            p.getClient().getOutgoingPacketBuilder()
                    .setRunEnergy((int) runEnergy)
                    .sendMessage("Your run energy is now: " + (int) runEnergy)
                    .send();
        }
    }

    /**
     * The formula for increase in run energy per second. As this is called every 600ms it is scaled to be
     * that of one world tick, or 600ms -> 6/10ths of a second.
     *
     * @param agilityLevel
     * @return
     */
    private double calculateIncreaseRunEnergy(int agilityLevel) {
        return ((8 + (agilityLevel / 6)) / 0.6 / 100) * (WorldConfig.WORLD_TICK_RATE_MS / 1000);
    }

    /**
     * The forumla for decrease run energy, called every time a player is running and has scaled two tiles.
     *
     * @param playerWeight
     * @return
     */
    private double calculateDecreaseRunEnergy(int playerWeight) {
        return (Math.min(playerWeight, 64) / 100) + 0.64;
    }

    public boolean isMoving() {
        return walkDirection != -1;
    }

    public boolean isRunning() {
        return runDirection != -1;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public int getWalkDirection() {
        return walkDirection;
    }

    public void resetMovement() {
        lastPosition = null;
        movementQueue.clear();
        walkDirection = -1;
        runDirection = -1;
    }

    public void beginMovement() {
        resetMovement();
        movementQueue.addFirst(e.getPosition());
    }

    public void finishMovement() {
        if (movementQueue.size() == 0) {
            throw new IllegalStateException("Movement has not begun");
        }
        lastPosition = movementQueue.poll();
    }

    public void stepTo(int x, int y) {
        Position entityPosition = movementQueue.peekLast();

        if (entityPosition == null) {
            throw new IllegalStateException("Movement has not begun");
        }

        int dx = x - entityPosition.getVector().getX();
        int dy = y - entityPosition.getVector().getY();

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
