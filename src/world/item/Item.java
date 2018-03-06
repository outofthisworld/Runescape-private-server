package world.item;

import util.integrity.Preconditions;
import world.definitions.DefinitionLoader;
import world.definitions.item.ItemDefinition;

public class Item implements IItem {
    private final ItemDefinition itemDefinition;
    private int id;
    private int amount;

    public Item(int id, int amount) {
        Preconditions.greaterThan(amount, 0);
        itemDefinition = DefinitionLoader.getDefinition(DefinitionLoader.ITEM_DEFINITIONS, id);
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

    public boolean canAddAmount(int amount) {
        long val = this.amount + amount;

        if (val > Integer.MAX_VALUE || val < 0) {
            return false;
        }

        return true;
    }

    public boolean canSubtractAmount(int amount) {
        return canAddAmount(-amount);
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }
}
