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

import util.Preconditions;
import world.entity.player.Player;
import world.item.Item;

import java.util.Objects;

public class Bank implements IContainer<Item> {
    private static final int BANK_SIZE = 200;
    private Player p;
    private Container<Item> bankItems;


    public Bank(Player p) {
        this.p = p;
        bankItems = new Container<>(Bank.BANK_SIZE, Item.class);
    }

    public boolean addBankItem(Item item) {
        Preconditions.notNull(item);

        if(bankItems.remaining() == 0){
            return false;
        }

        int nextSlot = bankItems.getNextFreeSlot();

        return false;
    }

    public boolean addBankItem(int id, int amount){
        Preconditions.greaterThanOrEqualTo(id,0);
        Preconditions.greaterThanOrEqualTo(amount,1);
        return addBankItem(new Item(id,amount));
    }

    public void removeBankItem(Item item) {

    }

    public void refresh(){

    }

    @Override
    public Container<Item> getContainer() {
        return bankItems;
    }
}
