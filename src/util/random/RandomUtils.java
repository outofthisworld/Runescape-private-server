package util.random;

import util.integrity.Preconditions;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random((System.nanoTime()) ^ (long) Math.random() * Long.MAX_VALUE | System.currentTimeMillis());

    public static Random getRandom() {
        return random;
    }

    /**
     * Generates a random integer that is greater than or equal to min and less than or equal to max.
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomIntBetween(int min, int max) {
        Preconditions.greaterThanOrEqualTo(max - min, 0);
        return min + random.nextInt(max - min + 1);
    }

    /**
     * Generates a sequence of random numbers between the given min and max value
     *
     * @param min
     * @param max
     * @param len
     * @return
     */
    public static int[] randomSequence(int min, int max, int len) {
        Preconditions.greaterThan(len, 0);
        return random.ints(len, min, max + 1).toArray();
    }
}
