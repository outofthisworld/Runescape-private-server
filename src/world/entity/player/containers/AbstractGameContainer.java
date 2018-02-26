package world.entity.player.containers;

import net.packets.outgoing.OutgoingPacketBuilder;
import world.entity.player.Player;
import world.item.IItem;
import world.item.Item;

import java.lang.reflect.Array;

public abstract class AbstractGameContainer<T extends IItem> {
    private int containerId;
    private Player player;
    private Container<T> container;
    private Class<T> containingType;


    public AbstractGameContainer(Player player, int size, int containerId, Class<T> klazz) {
        this.containerId = containerId;
        this.player = player;
        container = new Container(size, klazz);
        containingType = klazz;
    }


    public abstract boolean add(int slotId, int itemId, int amount);

    public abstract boolean add(int slotId, T item);

    public abstract boolean remove(int slotId, int itemId, int amount);

    public abstract boolean remove(int slotId, T item);

    public abstract boolean set(int slotId, int itemId, int amount);

    public abstract boolean set(int slotId, T item);

    public Player getOwner() {
        return player;
    }

    protected final Container<T> getContainer() {
        return container;
    }

    public T get(int slot) {
        return container.get(slot);
    }

    public int remaining() {
        return container.remaining();
    }

    public int capacity() {
        return container.capacity();
    }

    public void syncAll() {
        OutgoingPacketBuilder pBuilder = player.getClient().getOutgoingPacketBuilder();
        pBuilder.clearInventory(3214);

        T[] items = container.toArray((T[]) Array.newInstance(containingType, capacity()));

        int[] itemIds = new int[capacity()];
        int[] stackSizes = new int[capacity()];
        int[] slots = new int[capacity()];

        for (int i = 0; i < items.length; i++) {
            int itemId = items[i] == null ? 0 : items[i].getId();
            int stackSize = items[i] == null ? 0 : items[i].getAmount();
            slots[i] = i;
            itemIds[i] = itemId;
            stackSizes[i] = stackSize;
        }

        pBuilder.updateItems(3214, slots, itemIds, stackSizes, false).send();
    }

    public int getContainerId() {
        return containerId;
    }

    protected void sync(int slotId, int containerId, Item item) {
        sync(slotId, containerId, item.getItemDefinition().getId(), item.getAmount());
    }

    protected void sync(int slotId, int containerId, int itemId, int amount) {
        player.getClient().getOutgoingPacketBuilder().updateItem(containerId, slotId, itemId, amount).send();
    }

    protected void sync(int slotId, int itemId, int amount) {
        sync(slotId, containerId, itemId, amount);
    }

    protected void sync(int slotId, Item item) {
        sync(slotId, containerId, item.getItemDefinition().getId(), item.getAmount());
    }
}
