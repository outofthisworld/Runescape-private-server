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
import util.Preconditions;

import java.lang.reflect.Array;
import java.util.*;

public class Container<T> extends AbstractList<T> implements List<T> {

    private T[] items;
    int itemCount = 0;

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
            return;
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
        return items.length - itemCount;
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
    public int getFirstFreeSlot() {
        if(remaining() == 0){
            return -1;
        }
        for(int i = 0; i < items.length;i++){
            if(items[i] == null){
                return i;
            }
        }
        return -1;
    }

    public int getLastFreeSlot(){
        if(remaining() == 0){
            return -1;
        }
        for(int i = items.length-1; i >=0;i--){
            if(items[i] == null){
                return i;
            }
        }
        return -1;
    }


    @Override
    public boolean add(T t) {
        if(remaining() == 0){
            return false;
        }

        int slot = getFirstFreeSlot();
        if(slot == -1){
            return false;
        }

        items[slot] = t;
        return true;
    }

    @Override
    public T set(int index,
                 T element
    ) {
        T cur = items[index];
        if(cur == null && element != null){
            itemCount++;
        }
        if(cur != null && element == null){
            itemCount--;
        }
        items[index] = element;
        return cur;
    }

    @Override
    public void add(int index,
                    T element
    ) {

        throw new UnsupportedOperationException("Method not supported by this collection");
    }

    @Override
    public T remove(int index) {
        T t = items[index];
        items[index] = null;
        itemCount--;
        return t;
    }

    @Override
    public T get(int index) {
        return items[index];
    }


    @Override
    public int size() {
        return itemCount;
    }
}