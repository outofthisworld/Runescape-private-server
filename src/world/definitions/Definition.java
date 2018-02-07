package world.definitions;

public abstract class Definition<T extends Definition> {

    public abstract T getForId(int id);

    public abstract void load();
}
