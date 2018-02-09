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

import database.Database;
import net.Server;
import world.WorldManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The type Engine.
 */
public class Engine {
    private final ExecutorService e = Executors.newSingleThreadExecutor();
    private final Server server = new Server(EngineConfig.HOST, EngineConfig.PORT);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new Engine().start();
    }


    /**
     * Start.
     */
    public void start() {
        WorldManager.createWorld();
        Database.init();
        e.submit(this::startServer);
    }

    /**
     * Stop.
     */
    public void stop() {
        server.stop();
        WorldManager.shutdownWorlds();
        e.shutdownNow();
    }

    private void startServer() {
        int attempts = 0;
        while (true) {
            try {
                server.start();
            } catch (IOException e1) {
                e1.printStackTrace();
                attempts++;
                if (attempts == 5) {
                    stop();
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
