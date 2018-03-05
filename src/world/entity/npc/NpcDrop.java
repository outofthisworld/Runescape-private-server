package world.entity.npc;


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
    private String chance;

    public int getId() {
        return id;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public String getChanceName() {
        return chance;
    }

    public DropChance getChance() {
        DropChance dChance = DropChance.valueOf(chance);
        if (dChance == null) {
            throw new RuntimeException("Invalid drop chance");
        }
        return dChance;
    }
}
