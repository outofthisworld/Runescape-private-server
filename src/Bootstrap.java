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

import net.Reactor;
import world.WorldManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The type Bootstrap.
 */
public class Bootstrap implements Runnable {
    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor();
    private final Reactor reactor = new Reactor();

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new Bootstrap().boot();
    }


    /**
     * Start.
     */
    public void boot() {
        WorldManager.createWorld();
        networkExecutor.submit(this);
    }

    /**
     * Stop.
     */
    public void tearDown() {
        reactor.stop();
        WorldManager.shutdownWorlds();
        networkExecutor.shutdownNow();
    }

    @Override
    public void run() {
        int attempts = 0;
        while (true) {
            try {
                reactor.start();
            } catch (IOException e1) {
                e1.printStackTrace();
                attempts++;
                if (attempts == 5) {
                    tearDown();
                    break;
                } else {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}
