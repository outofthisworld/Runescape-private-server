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

package world;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;


/**
 * The type World manager.
 */
public class WorldManager {
    private static final CopyOnWriteArrayList<World> WORLDS = new CopyOnWriteArrayList<>();

    /**
     * Instantiates a new World manager.
     */
    public WorldManager() {

    }

    /**
     * Gets number of WORLDS.
     *
     * @return the number of WORLDS
     */
    public static int getNumberOfWorlds() {
        return WorldManager.WORLDS.size();
    }

    /**
     * Create world int.
     *
     * @return the int
     */
    public static int createWorld() {
        if (WorldManager.WORLDS.size() >= WorldConfig.MAX_VIRTUAL_WORLDS) {
            throw new InvalidStateException("Max no worlds exceeded");
        }

        World w = new World(WorldManager.WORLDS.size());
        WorldManager.WORLDS.add(w);
        //DOnt do anything with it for now, could be used later.
        w.start();
        return WorldManager.WORLDS.size() - 1;
    }

    /**
     * Shutdown worlds.
     */
    public static void shutdownWorlds() {
        WorldManager.WORLDS.forEach(World::stop);
    }


    /**
     * Get world world.
     *
     * @param worldId the world id
     * @return the world
     */
    public static World getWorld(int worldId) {
        return WorldManager.WORLDS.get(worldId);
    }


    /**
     * Submit task future.
     *
     * @param worldID the world id
     * @param r       the r
     * @return the future
     */
    public static Future<?> submitTask(int worldID, Runnable r) {
        return WorldManager.getWorld(worldID).submit(r);
    }

    /**
     * Submit task future.
     *
     * @param <T>     the type parameter
     * @param worldID the world id
     * @param r       the r
     * @return the future
     */
    public static <T> Future<T> submitTask(int worldID, Callable<T> r) {
        return WorldManager.getWorld(worldID).submit(r);
    }
}
