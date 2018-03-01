package world.item;

import world.definitions.item.ItemDefinition;

public interface IItem {
    int getAmount();

    int getId();

    ItemDefinition getItemDefinition();
}
