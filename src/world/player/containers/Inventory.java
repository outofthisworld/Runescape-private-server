package world.player.containers;

import world.item.Item;
import world.player.Player;

public class Inventory {
    private static final int INVENTORY_SIZE = 28;
    private final Player p;
    private final Container<Item> bankItems;


    public Inventory(Player p) {
        this.p = p;
        bankItems = new Container<>(Inventory.INVENTORY_SIZE, Item.class);
    }


    public int remaining(){
        return bankItems.remaining();
    }

}
