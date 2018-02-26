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

package world.item;

import sun.plugin.dom.exception.InvalidStateException;
import util.Preconditions;
import world.definitions.ItemDefinition;
import world.entity.player.Player;

import java.util.Optional;

public class Item {
    private int id;
    private int amount;
    private final ItemDefinition itemDefinition;

    public Item(int id, int amount) {
        Preconditions.greaterThan(amount,0);
        itemDefinition = ItemDefinition.getForId(id);
        Preconditions.notNull(itemDefinition);
        this.amount = amount;
        this.id = id;
    }

    public Item(Item item){
        this(item.getId(),item.getAmount());
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }
}
