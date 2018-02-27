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

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class Equipment extends AbstractGameContainer<Item> {


    public Equipment(Player p) {
        super(p, EquipmentSlot.values().length, 3214, Item.class);
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

    @Override
    public boolean add(int itemId, int amount) {
        return add(new Item(itemId,amount));
    }

    /**
     * Adds an item present from a users inventory into the specfied equipment slot.
     * @param slotId
     * @param item
     * @return
     */
    @Override
    public boolean add(Item item) {

        int slotId = item.getItemDefinition().getSlotId();

        if(slotId < 0 || slotId >= capacity() || item.getAmount() <= 0 || item.getId() <= 0){
            return false;
        }

        int slot = -1;

        for(int i = 0; i < getOwner().getInventory().capacity();i++){
            if(getOwner().getInventory().get(i) == item){
                slot = i;
                break;
            }
        }

        if(slot == -1){
            return false;
        }

        Item invItem = getOwner().getInventory().get(slot);
        getOwner().getInventory().remove(slot);
        sync(slotId,invItem);
        return true;
    }

    @Override
    public boolean remove(int slotId) {
        if(slotId < 0 || slotId >= capacity())
            return false;

        Item item = getContainer().get(slotId);

        if(item == null){
            return false;
        }

        sync(slotId,null);

        if(item.getItemDefinition().isStackable()){
            Inventory inv = getOwner().getInventory();

            OptionalInt slot = IntStream.range(0,capacity()).filter(i->{
                //Filter the ones that aren't this item
                if(inv.getContainer().get(i) == null || inv.get(i).getId() != item.getId()){
                    return false;
                }

                long newAmount = item.getAmount() + inv.get(i).getAmount();
                return newAmount <= Integer.MAX_VALUE;
            }).findFirst();
                if(slot.isPresent()){
                    //Append to the found stackable item
                    inv.set(slot.getAsInt(),new Item(item.getId(),
                            item.getAmount()+inv.getContainer().get(slot.getAsInt()).getAmount()));
                }else{
                    inv.add(item);
                }
        }

        return false;
    }


    @Override
    public boolean set(int slotId, int itemId, int amount) {
        return set(slotId,new Item(itemId,amount));
    }

    @Override
    public boolean set(int slotId, Item item) {
        return false;
    }
}
