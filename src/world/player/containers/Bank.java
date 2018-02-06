package world.player.containers;

import world.item.Item;
import world.player.Player;

public class Bank {
    private static final int BANK_SIZE = 200;
    private final Player p;
    private final Container<Item> bankItems;


    public Bank(Player p) {
        this.p = p;
        bankItems = new Container<>(Bank.BANK_SIZE, Item.class);
    }

    public void addBankItem(Item item) {

    }

    public void removeBankItem(Item item) {

    }
}
