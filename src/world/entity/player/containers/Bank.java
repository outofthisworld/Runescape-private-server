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

package world.entity.player.containers;

import world.entity.player.Player;
import world.item.Item;

public class Bank implements IContainer<Item>{
    private static final int BANK_SIZE = 200;
    private Player p;
    private Container<Item> bankItems;


    public Bank(Player p) {
        this.p = p;
        bankItems = new Container<>(Bank.BANK_SIZE, Item.class);
    }

    public void addBankItem(Item item) {

    }

    public void removeBankItem(Item item) {

    }

    @Override
    public Container<Item> getContainer() {
        return bankItems;
    }
}
