package world.entity.player.containers;

public interface IContainerItem<T extends IContainerItem<T>> {
     void setSlot(int containerSlot);
     void setContainerId(int containerId);
     int getSlot();
     int getContainerId();
     void setContainer(IContainer<T> container);
     IContainer<T> getContainer();
}
