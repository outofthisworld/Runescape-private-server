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
import net.impl.NetworkConfig;
import world.WorldConfig;
import world.WorldManager;
import world.definitions.DefinitionLoader;
import world.task.DefaultThreadFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
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
                }).whenCompleteAsync((aVoid, throwable) -> {
                    if(throwable != null){
                        throw new RuntimeException(throwable);
                    }
                    bootstrap.logger.log(Level.INFO,"[" + Bootstrap.class.getName() + "] successfully created game worlds.");
                });
            }
        },
        LOAD_DEFINITIONS(){
            @Override
            public CompletableFuture<Void> boot(Bootstrap bootstrap) {
                return DefinitionLoader.load().whenCompleteAsync((aVoid, throwable) -> {
                    if(throwable != null){
                        throw new RuntimeException(throwable);
                    }
                    bootstrap.logger.log(Level.INFO,"[" + Bootstrap.class.getName() + "] successfully loaded json definitions");
                });
            }
        },
        START_REACTOR(){
            @Override
            public CompletableFuture<Void> boot(Bootstrap bootstrap) {
                return CompletableFuture.runAsync(bootstrap, bootstrap.networkExecutor).whenCompleteAsync((aVoid, throwable) -> {
                    if(throwable != null){
                        throw new RuntimeException(throwable);
                    }
                    bootstrap.logger.log(Level.INFO,"[" + Bootstrap.class.getName() + "] reactor finished on " + NetworkConfig.HOST + " " + NetworkConfig.PORT);
                });
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
            tearDown().join();
            System.exit(0);
        }
    }

    /**
     * Tears down things in parallel.
     */
    public CompletableFuture<Void> tearDown() {
        return CompletableFuture.allOf(
                /**
                 Shutdown reactor,channels and channel handlers
                 */
                CompletableFuture.runAsync(reactor::stop),
                /**
                 Shutdown all game worlds
                 */
                CompletableFuture.runAsync(WorldManager::shutdownWorlds),
                /**
                 Shutdown the network executor
                 */
                CompletableFuture.runAsync(networkExecutor::shutdownNow)
        );
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
