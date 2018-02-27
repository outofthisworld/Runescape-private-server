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

import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Equipment extends AbstractGameContainer<Item> {


    public Equipment(Player p) {
        super(p, EquipmentSlot.values().length, 3214, Item.class);
    }


    @Override
    public boolean add(int itemId, int amount) {
        Item i = new Item(itemId, amount);
        return set(i.getItemDefinition().getSlotId(), i, i::equals);
    }

    /**
     * Adds an item present from a users inventory into the specfied equipment slot.
     *
     * @param slotId
     * @param item
     * @return
     */
    @Override
    public boolean add(Item item) {
        Preconditions.notNull(item);
        return set(item.getItemDefinition().getSlotId(), item, (i) -> i == item);
    }

    @Override
    public boolean remove(int slotId) {
        if (slotId < 0 || slotId >= capacity())
            return false;

        Item item = getContainer().get(slotId);

        if (item == null) {
            return false;
        }

        sync(slotId, null);

        if (item.getItemDefinition().isStackable()) {
            Inventory inv = getOwner().getInventory();

            OptionalInt slot = IntStream.range(0, capacity()).filter(i -> {
                //Filter the ones that aren't this item
                if (inv.getContainer().get(i) == null || inv.get(i).getId() != item.getId()) {
                    return false;
                }

                long newAmount = item.getAmount() + inv.get(i).getAmount();
                return newAmount <= Integer.MAX_VALUE;
            }).findFirst();
            if (slot.isPresent()) {
                //Append to the found stackable item
                inv.set(slot.getAsInt(), new Item(item.getId(),
                        item.getAmount() + inv.getContainer().get(slot.getAsInt()).getAmount()));
            } else {
                inv.add(item);
            }
        }

        return false;
    }

    @Override
    public boolean remove(int slotId, int amount) {
        return false;
    }

    @Override
    public boolean removeEqual(Item item) {
        return false;
    }

    @Override
    public boolean removeEqual(Item item, int amount) {
        return false;
    }

    @Override
    public boolean removeRef(Item item) {
        return false;
    }

    @Override
    public boolean removeRef(Item item, int amount) {
        return false;
    }


    @Override
    public boolean set(int slotId, int itemId, int amount) {
        Item i = new Item(itemId, amount);
        return set(slotId, new Item(itemId, amount), i::equals);
    }

    private boolean set(int slotId, Item item, Predicate<Item> pred) {
        Preconditions.notNull(item, pred);
        Preconditions.greaterThan(slotId, 0);

        int inventoryItemSlot = -1;
        for (int i = 0; i < getOwner().getInventory().getContainer().capacity(); i++) {
            if (pred.test(getOwner().getInventory().getContainer().get(i))) {
                inventoryItemSlot = i;
                break;
            }
        }

        if (inventoryItemSlot == -1) {
            return false;
        }

        //Check to see its a valid slot
        EquipmentSlot slot = EquipmentSlot.fromIndex(slotId);

        if (slot == null) {
            return false;
        }

        Item equipped = getContainer().get(slotId);
        Item inventoryItem = getOwner().getInventory().get(inventoryItemSlot);

        if (inventoryItem == null) {
            return false;
        }

        //Theres an item currently in that slot
        if (equipped == null) {
            getOwner().getInventory().removeRef(inventoryItem);
            sync(slotId, inventoryItem);
            return true;
        }

        //If this item is the same... add to it
        //If we cant add to it because it will overflow add as much as possible
        //If the item is different, remove the item in the slot to the inventory position it is being replaced by
        //and the inventory item into the slot.

        if (inventoryItem.getId() == equipped.getId() && inventoryItem.getItemDefinition().isStackable()) {

            if (equipped.addAmount(inventoryItem.getAmount())) {
                getOwner().getInventory().removeRef(inventoryItem);
                sync(slotId, equipped);
            } else {
                int remaining = Integer.MAX_VALUE - equipped.getAmount();
                if (remaining > 0) {
                    getOwner().getInventory().removeRef(inventoryItem, remaining);
                    equipped.addAmount(remaining);
                    sync(slotId, equipped);
                }
            }

        } else {
            getContainer().set(slotId, inventoryItem);
            getOwner().getInventory().set(inventoryItemSlot, equipped);
        }
        return true;
    }

    @Override
    public boolean set(int slotId, Item item) {
        return set(slotId, item, (i) -> i == item);
    }
}
