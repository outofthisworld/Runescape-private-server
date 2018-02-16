/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package world.entity;

import world.World;
import world.WorldManager;
import world.entity.misc.Position;

public abstract class Entity {
    /**
     * The entities position
     */
    protected final Position position = new Position(3232,3333,0);
    /**
     * The entities slot id
     */
    protected int slotId;
    /**
     * The world the player currently belongs to
     */
    protected int worldId;
    /**
     * Gets world id.
     *
     * @return the world id
     */
    public int getWorldId() {
        return worldId;
    }

    /**
     * Sets world id.
     *
     * @param worldId the world id
     */
    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    /**
     * Gets slot id.
     *
     * @return the slot id
     */
    public int getSlotId() {
        return slotId;
    }

    /**
     * Sets slot id.
     *
     * @param slotId the slot id
     */
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    /**
     * Gets world.
     *
     * @return the world
     */
    public World getWorld() {
        return WorldManager.getWorld(getWorldId());
    }


    public Position getPosition() {
        return position;
    }

    public abstract void poll();
}
