package database;

public interface DataStore<T, U> {
    T store(U o);
}
