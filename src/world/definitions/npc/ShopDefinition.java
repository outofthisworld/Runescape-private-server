package world.definitions.npc;

import world.definitions.types.GameCurrency;
import world.item.Item;

import java.util.ArrayList;
import java.util.List;


public class ShopDefinition {
    private List<Item> items = new ArrayList();
    private String name;
    private boolean restock;
    private boolean sellTo;
    private String currency;

    public String getName() {
        return name;
    }

    public boolean isRestock() {
        return restock;
    }

    public boolean isSellTo() {
        return sellTo;
    }

    public String getCurrencyName() {
        return currency;
    }

    public GameCurrency getCurrency(){
        GameCurrency currency = GameCurrency.valueOf(getCurrencyName());
        if(currency == null){
            throw new RuntimeException("Invalid game currency");
        }
        return currency;
    }


    public List<Item> getItems() {
        return items;
    }
}
