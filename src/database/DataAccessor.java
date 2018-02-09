package database;

public interface DataAccessor<T> {
    abstract T load(T obj);
}
