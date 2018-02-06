package world.containers;

import world.entity.player.EquipmentSlot;
import world.entity.player.Player;
import world.item.Item;

public class Equipment {
    private final Player p;
    private final Container<Item> equipment;


    public Equipment(Player p) {
        this.p = p;
        equipment = new Container<>(EquipmentSlot.values().length, Item.class);
    }

    public void equip(String slot, int itemId) {
        equip(EquipmentSlot.valueOf(slot), itemId);
    }

    public void equip(int slotId, int itemId) {
        equip(EquipmentSlot.fromIndex(slotId), itemId);
    }

    public void equip(EquipmentSlot e, int itemId) {

    }

    public void unEquip(EquipmentSlot e, int itemId) {
        if (p.getInventory().remaining() == 0) {
            p.getClient().getOutgoingPacketBuilder().sendMessage("Your inventory is currently full");
        }
    }

    public void refresh() {

    }
}
