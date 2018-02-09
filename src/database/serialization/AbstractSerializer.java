package database.serialization;

public abstract class AbstractSerializer<T, U> {
    public abstract U encode(T t);

    public abstract T decode(U s);

    public abstract Class<T> getSerializationClass();
}
