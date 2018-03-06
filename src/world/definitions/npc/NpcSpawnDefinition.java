package world.definitions.npc;

import world.definitions.IDefinition;
import world.entity.area.Vector;


/**
 * The type NpcDefinition spawn definition.
 */
public class NpcSpawnDefinition implements IDefinition {
    private int id;
    private Vector position;
    private boolean randomWalk;
    private int walkRadius;

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
     * Should random walk boolean.
     *
     * @return the boolean
     */
    public boolean shouldRandomWalk() {
        return randomWalk;
    }

    /**
     * Gets walk radius.
     *
     * @return the walk radius
     */
    public int getWalkRadius() {
        return walkRadius;
    }
}
