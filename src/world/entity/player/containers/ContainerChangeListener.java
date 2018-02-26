package world.entity.player.containers;

public interface ContainerChangeListener<T> {
    void accept(int slotId,int containerId,T item);
}
