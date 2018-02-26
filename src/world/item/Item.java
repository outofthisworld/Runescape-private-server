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

import util.Preconditions;
import world.definitions.ItemDefinition;

public class Item implements IItem {
    private final ItemDefinition itemDefinition;
    private int id;
    private int amount;

    public Item(int id, int amount) {
        Preconditions.greaterThan(amount, 0);
        itemDefinition = ItemDefinition.getForId(id);
        Preconditions.notNull(itemDefinition);
        this.amount = amount;
        this.id = id;
    }

    public Item(Item item) {
        this(item.getId(), item.getAmount());
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
