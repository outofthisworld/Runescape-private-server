package util.time;

import java.util.concurrent.TimeUnit;

/**
 * The type Stopwatch.
 */
public class Stopwatch {
    private long startTime;
    private long stopTime = -1;

    /**
     * Instantiates a new Stopwatch.
     */
    public Stopwatch() {
        restart();
    }

    /**
     * Restarts the stopwatch.
     */
    public void restart() {
        stopTime = -1;
        startTime = System.nanoTime();
    }

    /**
     * Gets the time passed since this stopwatch was created/restarted
     * Note that the stopwatch does not have to be stopped in order to call this method.
     * <p>
     * If the stopwatch is stopped, time passed is the difference between stop and start times
     * otherwise the current system time is used as the stop time (without stopping the stopwatch) and the difference between
     * the current time and the start time is returned.
     *
     * @param t the t
     * @return the time passed
     */
    public long getTimePassed(TimeUnit t) {
        long endTime = stopTime == -1 ? System.nanoTime() : stopTime;
        return t.convert(endTime - startTime, TimeUnit.NANOSECONDS);
    }

    /**
     * Stops the stopwatch.
     * Note that calling this method twice will have no affect and simply return.
     */
    public void stop() {
        if (stopTime != -1) {
            return;
        }
        stopTime = System.nanoTime();
    }
}
