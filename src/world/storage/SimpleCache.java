package world.storage;


import world.storage.DataAccessor;
import world.storage.DataStore;

import java.util.HashMap;

public abstract class SimpleCache<T, R> implements DataAccessor<T, R>, DataStore<Boolean, R> {

    protected final HashMap<T, R> cache = new HashMap<>();

    public boolean remove(Object o, Object value) {
        return cache.remove(o, value);
    }

    public R remove(Object o) {
        return cache.remove(o);
    }

    public boolean contains(Object o) {
        return cache.containsKey(o);
    }

    public void clear() {
        cache.clear();
    }
}
