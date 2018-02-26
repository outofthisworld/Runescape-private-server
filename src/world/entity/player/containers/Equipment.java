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
import world.definitions.ItemDefinition;
import world.entity.player.EquipmentSlot;
import world.entity.player.Player;
import world.item.Item;

public class Equipment {
    private final Player p;
    private final Container<Item> equipment;


    public Equipment(Player p) {
        this.p = p;
        equipment = new Container<It>(EquipmentSlot.values().length, Item.class);
    }


    /**
     * Equips an item in the specified slot.
     * Does not remove any items from the users inventory.
     */
    public boolean equip(EquipmentSlot slot, Item item) {
        Preconditions.notNull(slot, item);

        if (!item.getItemDefinition().isPresent()) {
            return false;
        }

        equipment.set(slot.getSlotId(), item);

    }

    /**
     * Equips an item in the users inventory at the specified slot.
     */
    public boolean equip(int inventorySlotId) {
        Inventory inventory = p.getInventory();

        Item inventoryItem = inventory.get(inventorySlotId);

        if (inventoryItem == null || !inventoryItem.getItemDefinition().isPresent()) {
            return false;
        }

        ItemDefinition inventoryItemDefinition = inventoryItem.getItemDefinition().get();

        if (!inventoryItemDefinition.isEquipable()) {
            return false;
        }

        int equipmentSlotId = inventoryItemDefinition.getSlotId();
        EquipmentSlot equipSlot = EquipmentSlot.fromIndex(equipmentSlotId);

        if (equipSlot == null) {
            return false;
        }


        //Check to see what we already have in that slot.
        if (!equipment.isEmpty(equipmentSlotId)) {
            Item equipped = equipment.get(equipmentSlotId);
            ItemDefinition equippedItemDef = equipped.getItemDefinition().get();

            //Check to see if the item is stackable.
            if (equipped.getItemDefinition().get().isStackable() &&
                    inventoryItemDefinition.getId() == equippedItemDef.getId()) {

                if (!equipped.addAmount(inventoryItem.getAmount())) {
                    int remaining = Math.min(Integer.MAX_VALUE - equipped.getAmount(), inventoryItem.getAmount());
                    if (!inventoryItem.addAmount(-remaining) && equipped.addAmount(remaining)) {
                        return false;
                    }
                }

            }
        }
        //
        inventory.remove(inventorySlotId, inventoryItem.getAmount());
        equip(equipSlot, inventoryItem);
        return true;

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
