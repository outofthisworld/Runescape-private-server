package util.random;

import java.math.BigDecimal;

public final class Chance {

    private Chance() {
    }

    /**
     * Returns true if the chance falls within the given percentage.
     * <p>
     * e.g returns true the contained psuedo random generator returns a value which falls
     * within the range of the given percentage.
     */
    public static boolean chanceWithin(double percent) {
        double rand = RandomUtils.getRandom().nextDouble();
        return BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100d))
                .subtract(BigDecimal.valueOf(rand))
                .compareTo(BigDecimal.valueOf(0d)) == -1 ? false : true;
    }
}
