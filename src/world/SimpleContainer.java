package world;

import util.integrity.Preconditions;

import java.util.*;

/**
 * The type Simple container.
 *
 * @param <T> the type parameter
 */
public class SimpleContainer<T> {
    /**
     * The Cursor.
     */
    protected int cursor;
    /**
     * The Max entities.
     */
    protected int maxEntities;
    /**
     * The Entities.
     */
    protected Map<Integer,T> entities;
    /**
     * The Free slots.
     */
    protected Set<Integer> freeSlots = new HashSet<>();

    /**
     * Instantiates a new Simple container.
     *
     * @param maxEntities the max entities
     */
    public SimpleContainer(int maxEntities){
        this.maxEntities = maxEntities;
        entities = new HashMap<>(maxEntities);
    }

    /**
     * Gets slot.
     *
     * @return the slot
     */
    public int getFreeSlot() {

        if(cursor >= maxEntities){
            return -1;
        }

        Optional<Integer> freeSlot = freeSlots.stream().findFirst();
        int slot;
        if (freeSlot.isPresent()) {
            slot = freeSlot.get();
        } else {
            slot = cursor;
        }

        return slot;
    }

    /**
     * Contains boolean.
     *
     * @param slot the slot
     * @return the boolean
     */
    public boolean contains(int slot){
        return entities.get(slot) != null;
    }

    /**
     * Get t.
     *
     * @param slot the slot
     * @return the t
     */
    public T get(int slot){
        return entities.get(slot);
    }

    /**
     * Add int.
     *
     * @param t the t
     * @return the int
     */
    public int add(T t){
        Preconditions.notNull(t);

        int slot = getFreeSlot();

        if(slot == -1){
            return -1;
        }

        cursor++;
        freeSlots.remove(slot);
        entities.put(slot,t);
        return slot;
    }

    /**
     * Remove boolean.
     *
     * @param slot the slot
     * @return the boolean
     */
    public boolean remove(int slot){
        Preconditions.greaterThanOrEqualTo(slot,0);
        Preconditions.lessThan(slot,maxEntities);

        if(!entities.containsKey(slot)) return false;

        entities.remove(slot);
        freeSlots.add(slot);
        return true;
    }

    /**
     * Get size int.
     *
     * @return the int
     */
    public int size(){
        return cursor - freeSlots.size();
    }

    /**
     * Get items immutable collection.
     *
     * @return the collection
     */
    public Collection<T> getItemsImmutable(){
        return Collections.unmodifiableCollection(entities.values());
    }

    /**
     * Gets free slots.
     *
     * @return the free slots
     */
    public int getRemaining() {
        return (maxEntities - cursor) + freeSlots.size();
    }
}
