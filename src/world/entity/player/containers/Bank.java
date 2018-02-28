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

public class Bank extends AbstractGameContainer<Item> {

    public Bank(Player p) {
        super(p, 600, 3214, Item.class);
    }

    @Override
    public boolean add(int itemId, int amount) {
        return add(new Item(itemId,amount));
    }

    @Override
    public boolean add(Item item) {


        return false;
    }

    @Override
    public boolean remove(int slotId) {
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
        return false;
    }

    @Override
    public boolean set(int slotId, Item item) {
        return false;
    }
}
