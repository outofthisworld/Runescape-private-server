package database;

import java.util.List;

public interface IDBAccessor<T> {

    boolean update(T t);

    boolean insert(T t);

    boolean delete(T t);

    <U> T findOne(U id);

    <U> List<T> findAll(U obj);
}
