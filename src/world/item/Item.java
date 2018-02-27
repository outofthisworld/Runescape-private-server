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

    public boolean addAmount(int amount){
        long val = this.amount + amount;

        if(val > Integer.MAX_VALUE || val < 0){
            return false;
        }

        this.amount = (int) val;
        return true;
    }

    public boolean subtractAmount(int amount){
        return addAmount(-amount);
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }
}
