package database;

import java.util.List;

/**
 * The interface Idb accessor.
 *
 * @param <T> the type parameter
 */
public interface IDBAccessor<T> {

    /**
     * Update boolean.
     *
     * @param id  the id
     * @param obj the obj
     * @return the boolean
     */
    boolean update(Object id, T obj);

    /**
     * Insert boolean.
     *
     * @param t the t
     * @return the boolean
     */
    boolean insert(T t);

    /**
     * Delete boolean.
     *
     * @param t the t
     * @return the boolean
     */
    boolean delete(T t);

    /**
     * Find one t.
     *
     * @param <U> the type parameter
     * @param id  the id
     * @return the t
     */
    <U> T findOne(U id);

    /**
     * Find one and populate t.
     *
     * @param <U> the type parameter
     * @param id  the id
     * @param obj the obj
     * @return the t
     */
    <U> T findOneAndPopulate(U id, T obj);

    /**
     * Find one and populate t.
     *
     * @param field the field
     * @param value the value
     * @param obj   the obj
     * @return the t
     */
    T findOneAndPopulate(String field, Object value, T obj);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<T> findAll();

    /**
     * Find list.
     *
     * @param limit the limit
     * @param skip  the skip
     * @return the list
     */
    List<T> find(int limit, int skip);
}
