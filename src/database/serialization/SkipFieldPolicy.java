package database.serialization;


import com.google.gson.FieldAttributes;

/**
 * The interface Skip field policy.
 */
public interface SkipFieldPolicy {
    /**
     * Should skip field boolean.
     *
     * @param fieldAttributes the field attributes
     * @return the boolean
     */
    boolean shouldSkipField(FieldAttributes fieldAttributes);

    /**
     * Should skip class boolean.
     *
     * @param aClass the a class
     * @return the boolean
     */
    default boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
