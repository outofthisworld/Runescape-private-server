package world.item;

import world.definitions.ItemDefinition;

public interface IItem {
    int getAmount();

    int getId();

    ItemDefinition getItemDefinition();
}
