package world.definitions.npc;

import world.definitions.IDefinition;
import world.definitions.types.GameCurrency;
import world.item.Item;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Shop definition.
 */
public class ShopDefinition implements IDefinition {
    private int id;
    private List<Item> items = new ArrayList();
    private String name;
    private boolean restock;
    private boolean sellTo;
    private GameCurrency currency;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Is restock boolean.
     *
     * @return the boolean
     */
    public boolean doesRestock() {
        return restock;
    }

    /**
     * Is sell to boolean.
     *
     * @return the boolean
     */
    public boolean canSellTo() {
        return sellTo;
    }

    /**
     * Gets currency name.
     *
     * @return the currency name
     */
    public GameCurrency getCurrency() {
        return currency;
    }


    @Override
    public int getId() {
        return id;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }
}
