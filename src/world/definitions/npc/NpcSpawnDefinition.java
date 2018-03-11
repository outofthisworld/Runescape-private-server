package world.definitions.npc;

import world.definitions.IDefinition;
import world.area.Area;
import world.area.Vector;


/**
 * The type NpcDefinition spawn definition.
 */
public class NpcSpawnDefinition implements IDefinition {
    private int id;
    private Vector position;
    private int radius;
    private String facing;



    public int getId() {
        return id;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * Gets walk radius.
     *
     * @return the walk radius
     */
    public int getWalkRadius() {
        return radius;
    }

    public String getFacing() {
        return facing;
    }


    public Area.TwoDimensional.Circle getNpcCircleArea(){
        return new Area.TwoDimensional.Circle(position.getX(),position.getY(),radius);
    }
}
