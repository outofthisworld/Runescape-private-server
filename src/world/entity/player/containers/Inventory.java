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
import sun.plugin.dom.exception.InvalidStateException;
import util.Preconditions;
import world.definitions.ItemDefinition;
import world.entity.player.Player;
import world.item.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Inventory implements IContainer<Item> {
    private static final int INVENTORY_SIZE = 28;
    private final Player p;
    private final Container<Item> inventoryItems;

    public Inventory(Player p) {
        this.p = p;
        inventoryItems = new Container<>(Inventory.INVENTORY_SIZE, Item.class);
    }

    public boolean add(Item item){
        Preconditions.notNull(item);

        if(item.getAmount() <= 0){
            return false;
        }


        //If its not a valid item, return.
        if(!item.getItemDefinition().isPresent()){
            return false;
        }

        ItemDefinition def = item.getItemDefinition().get();

        if(item.getAmount() > 1 && !def.isStackable() && !def.isNoted()){
            if(inventoryItems.remaining() < item.getAmount()){
                return false;
            }

            int[] itemIds = new int[item]

            for(int i = 0; i < item.getAmount();i++){
                int freeSlot = inventoryItems.getFirstFreeSlot();


                //Maybe change this to add group items
                if(inventoryItems.add(new Item(item.getId(),1))){
                    p.getClient().getOutgoingPacketBuilder().updateItem(3214, freeSlot,
                            item.getItemDefinition().get().getId(), 1).send();
                }else{
                    throw new InvalidStateException("Remaining was enough but add returned false");
                }
            }
            return true;
        }else{
            int freeSlot = inventoryItems.getFirstFreeSlot();
            if(inventoryItems.add(item)) {
                p.getClient().getOutgoingPacketBuilder().updateItem(3214, freeSlot,
                        item.getItemDefinition().get().getId(), item.getAmount()).send();
                return true;
            }else{
                return false;
            }
        }
    }

    public boolean add(int itemId, int amount) {
        Preconditions.greaterThanOrEqualTo(itemId,0);
        Preconditions.greaterThanOrEqualTo(amount,1);
        return add(new Item(itemId,amount));
    }


    public Item set(int slotId,int itemId,int amount){
        return set(slotId,new Item(itemId,amount));
    }

    public Item set(int slotId,Item item){
        OutgoingPacketBuilder pBuilder = p.getClient().getOutgoingPacketBuilder();
        Item replacedItem = null;

        //If the item is a valid item
        if(item != null && item.getId() >= 0 && item.getAmount() > 0 && item.getItemDefinition().isPresent()) {

            ItemDefinition itemDef = item.getItemDefinition().get();

            if(!itemDef.isNoted() && !itemDef.isStackable() && item.getAmount() > 1){
                item = new Item(item.getId(),1);
            }

            replacedItem = inventoryItems.set(slotId,item);
            pBuilder.updateItem(3214, slotId, item.getId(), item.getAmount());
        }

        if(replacedItem == null){
            replacedItem = inventoryItems.set(slotId,null);
            pBuilder.updateItem(3214, slotId, 0, 0);
        }

        pBuilder.send();
        return replacedItem;
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
            set(slotId,null);
        }else{
            set(slotId,item.getItemDefinition().get().getId(),remaining);
        }

        return true;
    }

    public void clear(){
        inventoryItems.clear();
        refresh();
    }

    public void refresh(){
        p.getClient().getOutgoingPacketBuilder().clearInventory(3214);
        Item[] items = getContainer().toArray(new Item[]{});

        int[] itemIds = new int[items.length];
        int[] stackSizes = new int[items.length];

        for(int i = 0; i < items.length;i++){
            int itemId = items[i] == null?0: items[i].getId();
            int stackSize = items[i] == null?0:items[i].getAmount();
            itemIds[i] = itemId;
            stackSizes[i] = stackSize;
        }

        p.getClient().getOutgoingPacketBuilder().updateItems(3214,itemIds,stackSizes);
    }

    @Override
    public Container<Item> getContainer() {
        return inventoryItems;
    }
}
