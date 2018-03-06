package world.entity.player.equipment;

import java.util.HashMap;

public enum EquipmentSlot {
    HEAD(0),
    CAPE(1),
    AMULET(2),
    WEAPON(3),
    CHEST(4),
    SHIELD(5),
    LEGS(7),
    HANDS(9),
    FEET(10),
    RING(12),
    ARROWS(13);


    private static final HashMap<Integer, EquipmentSlot> hm;

    static {
        EquipmentSlot[] slots = EquipmentSlot.values();
        hm = new HashMap<>(slots.length);
        for (int i = 0; i < slots.length; i++) {
            EquipmentSlot s = slots[i];
            EquipmentSlot.hm.put(s.getSlotId(), s);
        }
    }

    private final int slotId;

    EquipmentSlot(int slotId) {
        this.slotId = slotId;
    }

    public static EquipmentSlot fromIndex(int i) {
        return EquipmentSlot.hm.get(i);
    }

    public int getSlotId() {
        return slotId;
    }
}
