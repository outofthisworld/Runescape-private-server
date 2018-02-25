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

import world.definitions.ItemDefinition;

import java.util.Optional;

public class Item {

    private final ItemDefinition itemDefinition;
    private int amount;
    private int id;

    public Item(int id, int amount) {
        itemDefinition = ItemDefinition.getForId(id);
        this.amount = amount;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public Optional<ItemDefinition> getItemDefinition() {
        return Optional.ofNullable(itemDefinition);
    }
}
