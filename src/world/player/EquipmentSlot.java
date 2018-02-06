package world.player;

public enum EquipmentSlot {
    HEAD(0),
    CAPE(1),
    AMULET(2),
    WEAPON(3),
    CHEST(4),
    SHIELD(5),
    EMPTY(6),//Added in-case someone uses ordinal
    LEGS(7),
    HANDS(8),
    FEET(9),
    RING(10),
    BOOTS(11);

    private final int slotId;

    EquipmentSlot(int slotId) {
        this.slotId = slotId;
    }

    public static EquipmentSlot fromIndex(int i){
        return EquipmentSlot.values()[i];
    }

    public int getSlotId() {
        return slotId;
    }
}
