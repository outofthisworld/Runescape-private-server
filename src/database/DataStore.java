package database;

public interface DataStore<T, U> {
    T store(String key, U o);
}
