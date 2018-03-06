package world.entity.player.combat.magic;

import world.entity.player.Player;
import java.util.function.Consumer;


/**
 * The type Teleport spell.
 */
public class TeleportSpell extends AbstractSpell {
    private int x;
    private int y;
    private int z;
    private int interfaceButtonId;
    private Consumer<Player> action;

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets interface button id.
     *
     * @return the interface button id
     */
    public int getInterfaceButtonId() {
        return interfaceButtonId;
    }

    /**
     * Set action.
     *
     * @param consumer the consumer
     */
    public void setAction(Consumer<Player> consumer){
        this.action = consumer;
    }

    /**
     * Does the action assigned to this teleport spell.
     *
     * This will be used for opening interfaces/chatbox dialogues common in private servers.
     *
     * @param player the player
     */
    public void doAction(Player player){
        if(action != null){
            action.accept(player);
        }
    }

    /**
     * Teleports the given player to the teleport spells x,y,z coordinates.
     *
     *
     * Extract to interface Teleportable at a future time.
     * @param player the player
     */
    public void teleport(Player player){
        player.teleport(x,y,z);
    }
}