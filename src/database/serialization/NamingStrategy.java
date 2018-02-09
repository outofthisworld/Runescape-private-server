package database.serialization;

import java.lang.reflect.Field;

public interface NamingStrategy {
    String translateName(Field field);
}
