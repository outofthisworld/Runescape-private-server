package database.serialization;

import java.lang.reflect.Field;

/**
 * The interface Naming strategy.
 */
public interface NamingStrategy {
    /**
     * Translate name string.
     *
     * @param field the field
     * @return the string
     */
    String translateName(Field field);
}