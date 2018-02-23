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

import sun.plugin.dom.exception.InvalidStateException;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.HashSet;
import java.util.List;

public class Container<T> extends AbstractList<T> implements List<T> {

    private T[] items;
    private HashSet<Integer> removedIndex = new HashSet<>();
    private int cursor = 0;

    /**
     * Instantiates a new Container.
     *
     * @param size  the size
     * @param klazz the klazz
     */
    public Container(int size,
                     Class<T> klazz
    ) {
        items = (T[]) Array.newInstance(klazz, size);
    }

    @Override
    protected void removeRange(int fromIndex,
                               int toIndex
    ) {
        if (fromIndex > toIndex) {
            int temp = fromIndex;
            fromIndex = toIndex;
            toIndex = temp;
        }
        if (fromIndex < 0) {
            throw new IllegalArgumentException("Invalid from index");
        }
        if (toIndex > items.length) {
            throw new IllegalArgumentException("Invalid to index");
        }
        if (fromIndex == toIndex) {
            items[fromIndex] = null;
        }
        for (int i = fromIndex; i < toIndex; i++) {
            remove(i);
        }
    }

    /**
     * Remaining int.
     *
     * @return the int
     */
    public int remaining() {
        return items.length - cursor + removedIndex.size();
    }

    /**
     * Is empty boolean.
     *
     * @param index the index
     * @return the boolean
     */
    public boolean isEmpty(int index) {
        return items[index] == null;
    }


    /**
     * Get next free slot int.
     *
     * @return the int
     */
    public int getNextFreeSlot() {
        if(remaining() == 0){
            return -1;
        }
        return removedIndex.isEmpty() ? cursor : removedIndex.toArray(new Integer[]{})[0];
    }


    @Override
    public boolean add(T t) {
        if (remaining() == 0) {
            return false;
        }
        if (removedIndex.isEmpty()) {
            items[cursor++] = t;
        } else {
            int index = removedIndex.toArray(new Integer[]{})[0];
            items[index] = t;
            removedIndex.remove(index);
        }
        return true;
    }

    @Override
    public T set(int index,
                 T element
    ) {
        T cur = items[index];
        items[index] = element;
        return cur;
    }

    @Override
    public void add(int index,
                    T element
    ) {
        if (isEmpty(index)) {
            throw new InvalidStateException("Cannot add over a non null container item, use set.");
        }
        set(index, element);
    }

    @Override
    public T remove(int index) {
        T t = items[index];
        if (items[index] == null) {
            throw new InvalidStateException("Attempting to remove null item");
        }
        items[index] = null;
        removedIndex.add(index);
        return t;
    }

    @Override
    public void clear() {
        cursor = 0;
        removedIndex.clear();
        super.clear();
    }

    @Override
    public T get(int index) {
        return items[index];
    }

    @Override
    public int size() {
        return cursor - removedIndex.size();
    }
}