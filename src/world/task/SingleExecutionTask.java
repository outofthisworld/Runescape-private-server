package world.task;

import util.integrity.Preconditions;

public class SingleExecutionTask implements Task {

    private Runnable r;
    private int count = 1;

    public SingleExecutionTask(Runnable r) {
        Preconditions.notNull(r);
        this.r = r;
    }

    @Override
    public boolean check() {
        return count-- > 0 ? true : false;
    }

    @Override
    public void execute() {
        r.run();
    }

    @Override
    public boolean isFinished() {
        return count == 0;
    }
}
