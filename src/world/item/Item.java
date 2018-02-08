package world.item;

import world.definitions.ItemDefinition;

public class Item {

    private final ItemDefinition itemDefinition;
    private int amount;

    public Item(int id, int amount) {
        itemDefinition = ItemDefinition.getForId(id);
    }

    public int getAmount() {
        return amount;
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }
}
