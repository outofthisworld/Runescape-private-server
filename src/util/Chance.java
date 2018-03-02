package util;

import java.math.BigDecimal;
import java.util.Random;

public final class Chance {
    private static final Random random = new Random((System.nanoTime()) ^ (long) Math.random() * Long.MAX_VALUE | System.currentTimeMillis());


    private Chance() {
    }

    /**
     * Returns true if the chance falls within the given percentage.
     * <p>
     * e.g returns true the contained psuedo random generator returns a value which falls
     * within the range of the given percentage.
     */
    public static boolean chanceWithin(double percent) {
        double rand = random.nextDouble();
        System.out.println(rand);
        return BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100d))
                .subtract(BigDecimal.valueOf(rand))
                .compareTo(BigDecimal.valueOf(0d)) == -1 ? false : true;
    }

    public static Random getRandom() {
        return random;
    }
}
