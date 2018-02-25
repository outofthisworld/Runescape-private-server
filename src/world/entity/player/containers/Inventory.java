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

public class Inventory implements IContainer<Item> {
    private static final int INVENTORY_SIZE = 28;
    private final Player p;
    private final Container<Item> inventoryItems;

    public Inventory(Player p) {
        this.p = p;
        inventoryItems = new Container<>(Inventory.INVENTORY_SIZE, Item.class);
    }

    public boolean add(Item item) {
        Preconditions.notNull(item);

        if (item.getAmount() <= 0) {
            return false;
        }


        //If its not a valid item, return.
        if (!item.getItemDefinition().isPresent()) {
            return false;
        }

        ItemDefinition def = item.getItemDefinition().get();
        OutgoingPacketBuilder pBuilder = p.getClient().getOutgoingPacketBuilder();

        if (item.getAmount() > 1 && !def.isStackable() && !def.isNoted()) {
            if (inventoryItems.remaining() < item.getAmount()) {
                return false;
            }

            int[] itemIds = new int[item.getAmount()];
            int[] slots = new int[item.getAmount()];
            int[] sizes = new int[item.getAmount()];
            int addCount = 0;

            for (int i = 0; i < item.getAmount(); i++) {
                int freeSlot = inventoryItems.getFirstFreeSlot();
                int itemId = item.getItemDefinition().get().getId();
                int amount = 1;
                itemIds[i] = itemId;
                slots[i] = freeSlot;
                sizes[i] = amount;

                //Maybe change this to add group items
                if (inventoryItems.add(new Item(item.getId(), 1))) {
                    addCount++;
                } else {
                    break;
                }
            }

            //Double check that we actually added the correct amount of items
            if (addCount != item.getAmount()) {
                for (int i = 0; i < slots.length; i++) {
                    //Remove any added items from inv.
                    inventoryItems.remove(slots[i]);
                }
                return false;
            }
            //Update many items at once, and send as one big packet
            pBuilder.updateItems(3214, slots, itemIds, sizes, false).send();
            return true;

        }

        int freeSlot = inventoryItems.getFirstFreeSlot();

        if (!inventoryItems.add(item)) {
            return false;
        }

        //Update one item
        pBuilder.updateItem(3214, freeSlot,
                item.getItemDefinition().get().getId(), item.getAmount()).send();

        return true;
    }

    public boolean add(int itemId, int amount) {
        Preconditions.greaterThanOrEqualTo(itemId, 0);
        Preconditions.greaterThanOrEqualTo(amount, 1);
        return add(new Item(itemId, amount));
    }


    public Item set(int slotId, int itemId, int amount) {
        return set(slotId, new Item(itemId, amount));
    }

    public Item set(int slotId, Item item) {
        OutgoingPacketBuilder pBuilder = p.getClient().getOutgoingPacketBuilder();
        Item replacedItem = null;

        //If the item is a valid item
        if (item != null && item.getId() >= 0 && item.getAmount() > 0 && item.getItemDefinition().isPresent()) {

            ItemDefinition itemDef = item.getItemDefinition().get();

            if (!itemDef.isNoted() && !itemDef.isStackable() && item.getAmount() > 1) {
                item = new Item(item.getId(), 1);
            }

            replacedItem = inventoryItems.set(slotId, item);
            pBuilder.updateItem(3214, slotId, item.getId(), item.getAmount());
        }

        if (replacedItem == null) {
            replacedItem = inventoryItems.set(slotId, null);
            pBuilder.updateItem(3214, slotId, 0, 0);
        }

        pBuilder.send();
        return replacedItem;
    }

    public boolean remove(int slotId, int amount) {
        if (inventoryItems.isEmpty(slotId)) {
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
        inventoryItems.clear();
        refresh();
    }

    public void refresh() {
        OutgoingPacketBuilder pBuilder = p.getClient().getOutgoingPacketBuilder();
        pBuilder.clearInventory(3214);

        Item[] items = getContainer().toArray(new Item[]{});

        int[] itemIds = new int[items.length];
        int[] stackSizes = new int[items.length];
        int[] slots = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            int itemId = items[i] == null ? 0 : items[i].getId();
            int stackSize = items[i] == null ? 0 : items[i].getAmount();
            slots[i] = i;
            itemIds[i] = itemId;
            stackSizes[i] = stackSize;
        }

        pBuilder.updateItems(3214, slots, itemIds, stackSizes, false).send();
    }

    @Override
    public Container<Item> getContainer() {
        return inventoryItems;
    }
}
