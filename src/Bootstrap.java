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
import world.definitions.ItemDefinition;
import world.task.DefaultThreadFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The type Bootstrap.
 */
public class Bootstrap implements Runnable {
    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor(new DefaultThreadFactory(10));
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
        ItemDefinition.load();
        networkExecutor.submit(this);
       //run();
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
        try {
            reactor.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
