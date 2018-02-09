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

package world.task;

public abstract class WorldTask implements Task {

    /* The number of times this world.task should execute */
    private final int numExecutions;
    /*
        The successive number of ticks between each execution of this world.task
    */
    private final int repeatedDelayTicks;
    /*
        The number of ticks before this world.task first executes
    */
    private final int initialDelayTicks;
    /*
        If the initial delay should be
     */
    private final boolean repeatInitialDelay;
    /*
       The number of times this world.task should repeat

       One time is the whole cycle e.g initial delay - execute n times - sucessive delay - execute n times
    */
    private int repeatTimes;
    private int numExecutionsCount;
    private int repeatedDelayCount;
    private int initialDelayCount;

    public WorldTask(int numExecutions, int initialDelayTicks, int repeatedDelayTicks, int repeatTimes, boolean repeatInitialDelay) {
        this.numExecutions = numExecutions;
        this.initialDelayTicks = initialDelayTicks;
        this.repeatedDelayTicks = repeatedDelayTicks;
        this.repeatTimes = repeatTimes;
        this.repeatInitialDelay = repeatInitialDelay;
        numExecutionsCount = numExecutions;
        repeatedDelayCount = repeatedDelayTicks;
        initialDelayCount = initialDelayTicks;
    }

    @Override
    public boolean check() {

        //Wait until the initial delay has finished
        if (initialDelayCount > 0) {
            initialDelayCount--;
            return false;
        }


        if (initialDelayCount == 0 && numExecutionsCount > 0) {
            numExecutionsCount--;
            return true;
        }

        if (initialDelayCount == 0 && numExecutionsCount == 0) {
            numExecutionsCount = numExecutions;
        }

        if (repeatedDelayCount > 0) {
            repeatedDelayCount--;
            return false;
        }

        if (repeatedDelayCount == 0 && numExecutionsCount > 0) {
            numExecutionsCount--;
            return true;
        }

        if (repeatedDelayCount == 0 && numExecutionsCount == 0) {
            if (repeatTimes > 0) {
                repeatTimes--;
            }
            if (repeatInitialDelay) {
                initialDelayCount = initialDelayTicks;
            }
            numExecutionsCount = numExecutions;
            repeatedDelayCount = repeatedDelayTicks;
            if (repeatTimes != 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isFinished() {
        return repeatTimes == 0;
    }
}
