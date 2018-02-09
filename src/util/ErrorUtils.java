package util;

import java.util.Arrays;

public final class ErrorUtils {

    private ErrorUtils() {
    }

    public static void throwRuntimeIfAnyNull(RuntimeException t, Object... objects) {
        if (Arrays.stream(objects).filter(e -> e == null).count() > 0) {
            throw t;
        }
    }

}
