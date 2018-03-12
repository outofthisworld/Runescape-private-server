package util.integrity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * A utility class hosting a collection of method preconditions.
 */
public final class Preconditions {

    private Preconditions() {
    }

    public static void areReferentiallyEqual(Object one, Object two) {
        if (one != two) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void areEqual(Object one, Object two) {
        if (!one.equals(two)) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void throwIllegalArgumentException() {
        throw new IllegalArgumentException("Invalid params passed to method " + Thread.currentThread().getStackTrace()[3]);
    }

    public static void notNull(Object... objects) {
        Preconditions.notNull("Null object");
    }

    public static void notNull(String message, Object... objects) {
        for (Object o : objects) {
            if (o != null) {
                continue;
            }
            throw new IllegalArgumentException(message);
        }
    }

    public static void throwRuntimeIfAnyNull(RuntimeException t, Object... objects) {
        if (Arrays.stream(objects).filter(Objects::isNull).count() > 0) {
            throw t;
        }
    }

    /**
     * Amount must be greater than or equal to min and or less than or equal to max
     * <p>
     * e.g [0,1] means greater than or equal to 0 and less than or equal to 1
     */
    public static void inRangeClosed(int amount, int min, int max) {
        if (amount < min || amount > max) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void inRangeInclusive(int amount, int min, int max) {
        Preconditions.inRangeClosed(amount, min, max);
    }

    /**
     * Amount must be greater than min and less than max
     * e.g if amount is a value between min a max no error is thrown
     */
    public static void inRangeOpen(int amount, int min, int max) {
        if (amount <= min || amount >= max) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void inRangeExlusive(int amount, int min, int max) {
        Preconditions.inRangeOpen(amount, min, max);
    }

    public static void lessThanOrEqualTo(int amount, int check) {
        if (check > amount) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void lessThan(int amount, int check) {
        if (amount >= check) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void greaterThanOrEqualTo(int amount, int check) {
        if (amount < check) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void greaterThan(int amount, int check) {
        if (amount <= check) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void areEqual(int one, int two) {
        if (one != two) {
            Preconditions.throwIllegalArgumentException();
        }
    }

    public static void setContains(Object value, Set set) {
        if (set.contains(value)) {
            return;
        }
        Preconditions.throwIllegalArgumentException();
    }

}
