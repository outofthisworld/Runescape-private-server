package database.transform;

public class Item {

    private int itemId;
    private int amount;

    public Item(int itemId) {
        this(itemId, 1);
    }

    public Item(int itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
