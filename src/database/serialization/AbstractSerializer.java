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

package database.serialization;

public abstract class AbstractSerializer<T, U> {
    public abstract U encode(T t);

    public abstract T decode(U s);

    public abstract T decode(U string, T t);

    public abstract Class<T> getSerializationClass();
}
