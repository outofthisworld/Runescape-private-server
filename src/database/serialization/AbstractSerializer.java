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

/**
 * The type Abstract serializer.
 *
 * @param <T> the type parameter
 * @param <U> the type parameter
 */
public abstract class AbstractSerializer<T, U> {
    /**
     * Encode u.
     *
     * @param t the t
     * @return the u
     */
    public abstract U encode(T t);

    /**
     * Decode t.
     *
     * @param s the s
     * @return the t
     */
    public abstract T decode(U s);

    /**
     * Decode t.
     *
     * @param string the string
     * @param t      the t
     * @return the t
     */
    public abstract T decode(U string, T t);

    /**
     * Gets serialization class.
     *
     * @return the serialization class
     */
    public abstract Class<T> getSerializationClass();
}
