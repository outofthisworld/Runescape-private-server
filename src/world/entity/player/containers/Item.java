package world.entity.player.containers;


import sun.plugin.dom.exception.InvalidStateException;
import world.entity.player.Player;

public class Item implements IContainerItem<Item> {
    private int slot = -1;
    private int containerId = -1;
    private Player player;
    private int itemId;
    private int amount;
    private IContainer<Item> itemContainer = null;

    public Item(Player player, int itemId, int amount){
        this.player = player;
        this.itemId = itemId;
        this.amount = amount;

    }


    /**
     * Checks to see if this item is in a container and return true if it is.
     */
    public boolean isInContainer(){
        return getContainer() != null && slot > 0 && slot < itemContainer.getContainer().capacity()
                && itemContainer.getContainer().get(slot) == this;
    }

    /**
     * Sets the quantity of this container item, must be greater than zero.
     * @param amount
     * @return
     */
    public boolean setAmount(int amount){
        if(isInContainer()){
            return false;
        }

        if(amount <= 0){
            return false;
        }

        this.amount = amount;

    }



    public boolean refreshItem(){
        player.getClient()
                .getOutgoingPacketBuilder()
                .updateItem(containerId,slot,itemId,amount).send();
    }

    public boolean remove(){
        if(!isInContainer()){
            return false;
        }

        Item item = itemContainer.getContainer().set(slot,null);
        itemId = -1;
        amount = -1;

        if(item != this){
            throw new InvalidStateException("Removed item was not this item");
        }

        return true;
    }

    public boolean addAmount(int amount){
        if(!isInContainer()){
            return false;
        }


    }

    public boolean subtractAmount(int amount){
        return addAmount(-amount);
    }

    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void setSlot(int containerSlot) {
        this.slot = containerSlot;
    }

    @Override
    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public int getContainerId() {
        return containerId;
    }

    @Override
    public void setContainer(IContainer<Item> container) {
        itemContainer = container;
    }

    @Override
    public IContainer<Item> getContainer() {
        return itemContainer;
    }
}
