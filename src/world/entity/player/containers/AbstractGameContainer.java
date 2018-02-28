package world.entity.player.containers;

import net.packets.outgoing.OutgoingPacketBuilder;
import util.Preconditions;
import world.entity.player.Player;
import world.item.IItem;
import world.item.Item;

import java.lang.reflect.Array;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractGameContainer<T extends IItem> implements IContainer<T> {
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

    /**
     * Adds an item of the specified amount to the specified slot.
     * <p>
     * This method will contain the login for adding an item to the specified slot.
     * For example, an equipment containers add method will only return true
     * if and only if the item is also present in the users inventory.
     * <p>
     * If an item is to be added without logic, the set method can be used.
     *
     * @param slotId
     * @param itemId
     * @param amount
     * @return
     */
    public abstract boolean add(int itemId, int amount);

    public abstract boolean add(T item);

    public abstract boolean remove(int slotId);

    public abstract boolean remove(int slotId, int amount);

    public abstract boolean removeEqual(Item item);

    public abstract boolean removeEqual(Item item, int amount);

    public abstract boolean removeRef(Item item);

    public abstract boolean removeRef(Item item, int amount);

    public abstract boolean set(int slotId, int itemId, int amount);

    public abstract boolean set(int slotId, T item);

    public Player getOwner() {
        return player;
    }

    public final Container<T> getContainer() {
        return container;
    }

    public T get(int slot) {
        if (slot < 0 || slot >= capacity()) return null;
        return container.get(slot);
    }

    public int indexOf(Predicate<T> pred) {
        Preconditions.notNull(pred);
        for (int i = 0; i < capacity(); i++) {
            if (get(i) != null && pred.test(get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfRef(T item) {
        Preconditions.notNull(item);
        return indexOf((i) -> i == item);
    }

    public int indexOfEquals(T item) {
        Preconditions.notNull(item);
        return indexOf(item::equals);
    }

    public void forEach(BiConsumer<Integer, T> consumer) {
        for (int i = 0; i < capacity(); i++) {
            consumer.accept(i, get(i));
        }
    }

    public Stream stream() {
        return getContainer().stream();
    }

    public int remaining() {
        return container.remaining();
    }

    public int capacity() {
        return container.capacity();
    }

    public void syncAll() {
        OutgoingPacketBuilder pBuilder = player.getClient().getOutgoingPacketBuilder();
        pBuilder.clearInventory(containerId);

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

        syncAll(slots, itemIds, stackSizes, false);
    }


    public void syncAll(int[] slots, int[] itemIds, int[] stackSizes, boolean ignoreNulls) {
        player.getClient().getOutgoingPacketBuilder().updateItems(containerId, slots, itemIds, stackSizes, ignoreNulls).send();
    }

    public int getContainerId() {
        return containerId;
    }

    protected void sync(int slotId) {
        sync(slotId, get(slotId));
    }

    protected void sync(int slotId, int containerId, T item) {
        int itemId = 0;
        int amount = 0;

        if (item == null || item.getId() <= 0) {
            getContainer().set(slotId, null);
        } else {
            getContainer().set(slotId, item);
            itemId = item.getId();
            amount = item.getAmount();
        }

        player.getClient().getOutgoingPacketBuilder().updateItem(containerId, slotId, itemId, amount).send();
    }

    protected void sync(int slotId, T item) {
        sync(slotId, containerId, item);
    }

}
