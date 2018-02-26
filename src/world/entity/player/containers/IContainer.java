package world.entity.player.containers;

public interface IContainer<T extends IContainerItem<T>> {
    Container<T> getContainer();
}
