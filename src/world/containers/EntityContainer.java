package world.containers;

import util.integrity.Preconditions;
import world.entity.Entity;

public class EntityContainer<T extends Entity> extends SimpleContainer<T> {
    public EntityContainer(int maxEntities) {
        super(maxEntities);
    }

    public boolean remove(T entity){
        Preconditions.notNull(entity);

        int slot = entity.getSlotId();
        boolean removed =  remove(slot);
        if(removed) {
            entity.setSlotId(-1);
            entity.setWorldId(-1);
        }

        return removed;
    }

    @Override
    public int add(T t) {
        int id =  super.add(t);
        t.setSlotId(id);
        return id;
    }
}
