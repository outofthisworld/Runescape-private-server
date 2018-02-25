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

    public int add(Item item){
        Preconditions.notNull(item);
        if(!item.getItemDefinition().isPresent()){
            return -1;
        }

        int slot = inventoryItems.getNextFreeSlot();
        if(inventoryItems.add(item)) {
            p.getClient().getOutgoingPacketBuilder().updateSingleItem(3214, slot,
                    item.getItemDefinition().get().getId(), item.getAmount()).send();
            return slot;
        }else{
            return -1;
        }
    }

    public int add(int itemId, int amount) {
        Preconditions.greaterThanOrEqualTo(itemId,0);
        Preconditions.greaterThanOrEqualTo(amount,1);
        return add(new Item(itemId,amount));
    }

    public int remove(Item item){
        Preconditions.notNull(item);
        if(!item.getItemDefinition().isPresent()){
            return -1;
        }

        inventoryItems.re
    }

    public boolean set(int slotId,int itemId,int amount){
        return set(slotId,new Item(itemId,amount));
    }

    public boolean set(int slotId,Item item){
        inventoryItems.set(slotId,item);
        p.getClient().getOutgoingPacketBuilder().updateSingleItem(3214, slotId, item.getId(), item.getAmount());
    }

    public boolean remove(int slotId, int amount){
        if(inventoryItems.isEmpty(slotId)){
            return false;
        }

        Item item = inventoryItems.remove(slotId);
        int remaining = item.getAmount() - amount;

        if(remaining < 0){
            return false;
        }

        if(remaining == 0){
            p.getClient().getOutgoingPacketBuilder().updateSingleItem(3214, slotId, -1, 0);
        }else{
            add(item.getItemDefinition().get().getId(),remaining);
        }

        return true;
    }



    public void refresh(){

    }


    public int remaining() {
        return inventoryItems.remaining();
    }

    @Override
    public Container<Item> getContainer() {
        return inventoryItems;
    }
}
