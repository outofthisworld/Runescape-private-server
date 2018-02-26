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

import java.util.Optional;

public class Item {

    private final ItemDefinition itemDefinition;
    private int amount;
    private int id;

    public Item(int id, int amount) {
        Preconditions.greaterThan(amount,0);
        itemDefinition = ItemDefinition.getForId(id);
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

    public boolean addAmount(int amount){
        long sum = this.amount + amount;

        if(sum > Integer.MAX_VALUE || sum < 0){
            return false;
        }

        this.amount = (int) sum;
        return true;
    }

    public Optional<ItemDefinition> getItemDefinition() {
        return Optional.ofNullable(itemDefinition);
    }
}
