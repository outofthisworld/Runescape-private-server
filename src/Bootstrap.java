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
import world.WorldConfig;
import world.WorldManager;
import world.definitions.DefinitionLoader;
import world.definitions.ItemDefinition;
import world.task.DefaultThreadFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * The type Bootstrap.
 */
public class Bootstrap implements Runnable {
    private static final Logger logger = Logger.getLogger(Bootstrap.class.getName());
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

    private enum BootTasks{
        LOAD_WORLDS(){
            @Override
            public CompletableFuture<Void> boot(Bootstrap bootstrap) {
                return CompletableFuture.runAsync(()->{
                    for(int i = 0; i < WorldConfig.NUM_VIRTUAL_WORLDS; i++){
                        WorldManager.createWorld();
                    }
                });
            }
        },
        LOAD_DEFINITIONS(){
            @Override
            public CompletableFuture<Void> boot(Bootstrap bootstrap) {
                return DefinitionLoader.load();
            }
        },
        START_REACTOR(){
            @Override
            public CompletableFuture<Void> boot(Bootstrap bootstrap) {
                return CompletableFuture.runAsync(bootstrap, bootstrap.networkExecutor);
            }
        };
        public abstract CompletableFuture<Void> boot(Bootstrap bootstrap);
    }


    /**
     * Start.
     */
    public void boot() {
        /*
            Run all boot tasks, and throw a runtime exception if any complete exceptionally.
         */
        try {
            CompletableFuture.allOf(Stream.of(BootTasks.values()).map(e -> e.boot(this))
                    .collect(Collectors.toList()).toArray(new CompletableFuture[]{})).join();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
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
            //Caught later down the line when boot tasks are joined.
            throw new RuntimeException(e);
        }
    }
}
