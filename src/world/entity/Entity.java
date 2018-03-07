package world.entity;

import world.World;
import world.WorldConfig;
import world.WorldManager;
import world.combat.CombatHandler;
import world.entity.area.Position;
import world.entity.movement.Movement;

/**
 * The type Entity.
 */
public abstract class Entity {
    /**
     * The entities position
     */
    protected final Position position = new Position(WorldConfig.PLAYER_START_X, WorldConfig.PLAYER_START_Y, WorldConfig.PLAYER_START_Z);
    /**
     * The Movement.
     */
    protected final Movement movement = new Movement(this);
    /**
     * The entities slot id
     */
    protected int slotId;
    /**
     * The world the player currently belongs to
     */
    protected int worldId;
    /**
     * The weight of this player
     **/
    protected int weight;

    private final CombatHandler combatHandler = new CombatHandler(this);


    public CombatHandler getCombatHandler(){
        return combatHandler;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets weight.
     *
     * @param weight the weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Gets movement.
     *
     * @return the movement
     */
    public Movement getMovement() {
        return movement;
    }

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


    /**
     * Gets position.
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }


    /**
     * Is player boolean.
     *
     * @return the boolean
     */
    public boolean isPlayer() {
        return false;
    }

    /**
     * Poll.
     */
    public abstract void poll();
}
