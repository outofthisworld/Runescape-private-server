/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package database;

import java.util.List;

public interface IDBAccessor<T> {

    boolean update(Object id, T obj);

    boolean insert(T t);

    boolean delete(T t);

    <U> T findOne(U id);

    List<T> findAll();

    List<T> find(int limit, int skip);
}
