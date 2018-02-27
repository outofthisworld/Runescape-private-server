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

    public Inventory(Player player) {
        super(player, 28, 3214, Item.class);
    }

    @Override
    public boolean add(int itemId, int amount) {
        return add(new Item(itemId,amount));
    }

    public boolean add(Item item) {
        Preconditions.notNull(item);

        ItemDefinition def = item.getItemDefinition();

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
            syncAll(slots,itemIds,sizes,false);
            return true;

        }

        int freeSlot = getContainer().getFirstFreeSlot();

        if(freeSlot == -1){
            return false;
        }

        sync(freeSlot,item);
        return true;
    }

    @Override
    public boolean remove(int slotId) {
        return remove(slotId,get(slotId).getAmount());
    }

    @Override
    public boolean remove(int slotId, int amount) {
        if(slotId < 0 || slotId >= capacity())
            return false;

        Item i = get(slotId);
        if(i == null){
            return false;
        }

        if(!i.subtractAmount(amount)){
            return false;
        }

        sync(slotId,i);
        return true;
    }

    @Override
    public boolean removeRef(Item item) {
        Preconditions.notNull(item);
        return removeRef(item,item.getAmount());
    }

    @Override
    public boolean removeRef(Item item, int amount) {
        Preconditions.notNull(item);
        for(int i = 0; i < capacity();i++){
            if(get(i) == item){
                return remove(i,amount);
            }
        }
        return false;
    }

    @Override
    public boolean removeEqual(Item item) {
        Preconditions.notNull(item);
        return removeEqual(item,item.getAmount());
    }

    @Override
    public boolean removeEqual(Item item, int amount) {
        Preconditions.notNull(item);
        for(int i = 0; i < capacity();i++){
            if(get(i).equals(item)){
                return remove(i,amount);
            }
        }
        return false;
    }

    public boolean set(int slotId, int itemId, int amount) {
        return set(slotId, new Item(itemId, amount));
    }

    public boolean set(int slotId, Item item) {
        Preconditions.notNull(item);

        //If the item is a valid item
        if (item != null && item.getId() >= 0 && item.getAmount() > 0) {

            ItemDefinition itemDef = item.getItemDefinition();

            if (!itemDef.isNoted() && !itemDef.isStackable() && item.getAmount() > 1) {
                item = new Item(item.getId(), 1);
            }
        }

        sync(slotId,item);
        return true;
    }

    public void clear() {
        getContainer().clear();
        syncAll();
    }
}
