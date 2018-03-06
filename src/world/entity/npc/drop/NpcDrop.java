package world.entity.npc.drop;


/**
 * {
 * "id": 4151,
 * "minimum": 1,
 * "maximum": 1,
 * "chance": "RARE"
 * }
 */
public class NpcDrop {
    private int id;
    private int minimum;
    private int maximum;
    private DropChance chance;

    public int getId() {
        return id;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public DropChance getDropChance() {
        return chance;
    }

}
