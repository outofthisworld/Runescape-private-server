package database.serialization;

public interface Encoder<T, R> {
    R encode(T t);
}
