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

package world.entity.player.containers;

import util.Preconditions;
import world.entity.player.EquipmentSlot;
import world.entity.player.Player;
import world.item.Item;

public class Equipment implements IContainer<Item> {
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
        Preconditions.notNull(e);
        Preconditions.greaterThan(itemId, 0);
    }

    public void unEquip(EquipmentSlot e, int itemId) {
        if (p.getInventory().getContainer().remaining() == 0) {
            p.getClient().getOutgoingPacketBuilder().sendMessage("Your inventory is currently full");
        }
    }

    public Container<Item> getContainer() {
        return equipment;
    }

    public void refresh() {

    }
}
