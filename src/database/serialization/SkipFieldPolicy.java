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


import com.google.gson.FieldAttributes;

public interface SkipFieldPolicy {
    boolean shouldSkipField(FieldAttributes fieldAttributes);

    default boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
