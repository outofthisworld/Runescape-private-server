package world.entity.player;

public enum AppearanceSlot {
    HEAD(0),
    TORSO(1),
    ARMS(2),
    HANDS(3),
    LEGS(4),
    FEET(5);

    private int id;

    AppearanceSlot(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
