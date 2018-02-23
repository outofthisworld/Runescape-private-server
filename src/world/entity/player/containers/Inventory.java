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

import world.entity.player.Player;
import world.item.Item;

public class Inventory implements IContainer<Item> {
    private static final int INVENTORY_SIZE = 28;
    private final Player p;
    private final Container<Item> bankItems;


    public Inventory(Player p) {
        this.p = p;
        bankItems = new Container<>(Inventory.INVENTORY_SIZE, Item.class);
    }


    public int remaining() {
        return bankItems.remaining();
    }

    @Override
    public Container<Item> getContainer() {
        return bankItems;
    }
}
