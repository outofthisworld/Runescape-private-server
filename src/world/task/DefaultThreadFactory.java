package world.task;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.concurrent.ThreadFactory;

public class DefaultThreadFactory implements ThreadFactory {
    private final int count = 0;
    private final int priority;

    /**
     * Instantiates a new World thread factory.
     *
     * @param priority the priority
     */
    public DefaultThreadFactory(int priority) {

        if (priority > Thread.MAX_PRIORITY || priority < Thread.MIN_PRIORITY) {
            throw new InvalidStateException("Invalid priority for world thread.");
        }

        this.priority = priority;
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("World " + count + " thread");
        t.setPriority(priority);
        t.setUncaughtExceptionHandler((t1, e) -> {
            System.out.println("error");
            e.printStackTrace();
        });
        return t;
    }
}