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

import net.packets.outgoing.OutgoingPacketBuilder;
import util.Preconditions;
import world.definitions.ItemDefinition;
import world.entity.player.Player;
import world.item.Item;


public class Inventory extends AbstractGameContainer<Item> {
    private static final int INVENTORY_SIZE = 28;

    public Inventory(Player player) {
        super(player, INVENTORY_SIZE, 3214, Item.class);
    }

    public boolean add(Item item) {
        Preconditions.notNull(item);

        if (item.getAmount() <= 0) {
            return false;
        }

        ItemDefinition def = item.getItemDefinition();
        OutgoingPacketBuilder pBuilder = p.getClient().getOutgoingPacketBuilder();

        if (item.getAmount() > 1 && !def.isStackable() && !def.isNoted()) {
            if (remaining() < item.getAmount()) {
                return false;
            }

            int[] itemIds = new int[item.getAmount()];
            int[] slots = new int[item.getAmount()];
            int[] sizes = new int[item.getAmount()];
            int addCount = 0;

            for (int i = 0; i < item.getAmount(); i++) {
                int freeSlot = getContainer().getFirstFreeSlot();
                int itemId = item.getItemDefinition().getId();
                int amount = 1;
                itemIds[i] = itemId;
                slots[i] = freeSlot;
                sizes[i] = amount;

                //Maybe change this to add group items
                if (getContainer().add(new Item(item.getId(), 1))) {
                    addCount++;
                } else {
                    break;
                }
            }

            //Double check that we actually added the correct amount of items
            if (addCount != item.getAmount()) {
                for (int i = 0; i < slots.length; i++) {
                    //Remove any added items from inv.
                    getContainer().remove(slots[i]);
                }
                return false;
            }
            //Update many items at once, and send as one big packet
            pBuilder.updateItems(3214, slots, itemIds, sizes, false).send();
            return true;

        }

        int freeSlot = getContainer().getFirstFreeSlot();

        if (!getContainer().add(item)) {
            return false;
        }

        //Update one item
        pBuilder.updateItem(3214, freeSlot,
                item.getItemDefinition().getId(), item.getAmount()).send();

        return true;
    }

    public boolean add(int itemId, int amount) {
        Preconditions.greaterThanOrEqualTo(itemId, 0);
        Preconditions.greaterThanOrEqualTo(amount, 1);
        return add(new Item(itemId, amount));
    }


    @Override
    public boolean add(int slotId, int itemId, int amount) {
        return false;
    }

    @Override
    public boolean add(int slotId, Item item) {
        return false;
    }

    @Override
    public boolean remove(int slotId, int itemId, int amount) {
        return false;
    }

    @Override
    public boolean remove(int slotId, Item item) {
        return false;
    }

    public boolean set(int slotId, int itemId, int amount) {
        return set(slotId, new Item(itemId, amount));
    }

    public boolean set(int slotId, Item item) {
        Preconditions.notNull(item);
        OutgoingPacketBuilder pBuilder = getOwner().getClient().getOutgoingPacketBuilder();
        Item replacedItem = null;

        //If the item is a valid item
        if (item != null && item.getId() >= 0 && item.getAmount() > 0) {

            ItemDefinition itemDef = item.getItemDefinition();

            if (!itemDef.isNoted() && !itemDef.isStackable() && item.getAmount() > 1) {
                item = new Item(item.getId(), 1);
            }

            replacedItem = getContainer().set(slotId, item);
            pBuilder.updateItem(3214, slotId, item.getId(), item.getAmount());
        }

        if (replacedItem == null) {
            replacedItem = getContainer().set(slotId, null);
            pBuilder.updateItem(3214, slotId, 0, 0);
        }

        pBuilder.send();
        return true;
    }


    public boolean remove(int slotId, int amount) {
        if (getContainer().isEmpty(slotId)) {
            return false;
        }

        Item item = inventoryItems.remove(slotId);
        int remaining = item.getAmount() - amount;

        if (remaining < 0) {
            return false;
        }

        if (remaining == 0) {
            set(slotId, null);
        } else {
            set(slotId, item.getItemDefinition().get().getId(), remaining);
        }

        return true;
    }

    public void clear() {
        getContainer().clear();
        syncAll();
    }
}
