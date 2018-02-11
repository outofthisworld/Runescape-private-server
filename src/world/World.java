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

import net.impl.decoder.GamePacketDecoder;
import net.impl.decoder.LoginProtocolConstants;
import sun.plugin.dom.exception.InvalidStateException;
import world.entity.player.Player;
import world.event.Event;
import world.event.EventBus;
import world.event.WorldEventBus;
import world.event.impl.ClientDisconnectEvent;
import world.event.impl.PlayerLoginEvent;
import world.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * The type World.
 */
public class World {
    private final HashMap<Integer, Player> players = new HashMap(WorldConfig.MAX_PLAYERS_IN_WORLD);
    private final ConcurrentLinkedQueue<Task> worldTasks = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService worldExecutorService = Executors.newSingleThreadScheduledExecutor(new WorldThreadFactory(10));
    private final int worldId;
    private final HashSet<Integer> freePlayerSlots = new HashSet<>();
    private final EventBus eventBus = new WorldEventBus(this);
    private ScheduledFuture<?> worldExecutionTask;
    private int playersCount = 0;

    /**
     * Instantiates a new World.
     *
     * @param worldId the world id
     */
    World(int worldId) {
        this.worldId = worldId;
        getEventBus().register(this);
    }


    /**
     * Start scheduled future.
     *
     * @return the scheduled future
     */
    ScheduledFuture<?> start() {
        return worldExecutionTask = worldExecutorService.scheduleAtFixedRate(this::poll, 0, WorldConfig.WORLD_TICK_RATE_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets event bus.
     *
     * @return the event bus
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Stop.
     */
    void stop() {
        worldExecutionTask.cancel(true);
        worldTasks.clear();
    }

    /**
     * Gets slot.
     *
     * @return the slot
     */
    private int getSlot() {

        if (!(playersCount < WorldConfig.MAX_PLAYERS_IN_WORLD)) {
            return -1;
        }

        Optional<Integer> freeSlot = freePlayerSlots.stream().findFirst();
        int playerIndex;
        if (freeSlot.isPresent()) {
            playerIndex = freeSlot.get();
            freePlayerSlots.remove(playerIndex);
        } else {
            playerIndex = playersCount;
            playersCount++;
        }

        return playerIndex;
    }

    /**
     * Add.
     *
     * @param slot the slot
     * @param p    the p
     */
    private void addPlayerToWorld(int slot, Player p) {
        if (players.containsKey(slot)) {
            throw new IllegalArgumentException("Player slot was not null");
        }

        players.put(slot, p);
    }

    @Event()
    private void playerLoginEvent(PlayerLoginEvent lEvent) {

        if (lEvent.getPlayer() == null) {
            return;
        }

        int loginSlot = getSlot();

        if (loginSlot == -1) {
            //world full 7
            lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.WORLD_FULL, 0);
            return;
        }

        if (getPlayerByName(lEvent.getPlayer().getUsername()).isPresent()) {
            //already logged in 5
            lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.ALREADY_LOGGED_IN, 0);
            return;
        }

        lEvent.getPlayer().load().thenAcceptAsync(player -> {
            Player deserialized;
            if (player.isPresent()) {
                deserialized = player.get();
                if (!deserialized.getPassword().equals(deserialized.getPassword())) {
                    lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.INVALID_USERNAME_OR_PASSWORD, 0);
                    return;
                }

                if (deserialized.isDisabled()) {
                    lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.ACCOUNT_DISABLED, 0);
                    return;
                }
            } else {
                deserialized = lEvent.getPlayer();
                Player.asyncPlayerStore().store(deserialized);
            }

            deserialized.setSlotId(loginSlot);
            deserialized.setWorldId(worldId);
            addPlayerToWorld(loginSlot, deserialized);
            deserialized.getClient().setProtocolDecoder(new GamePacketDecoder());
            lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.LOGIN_SUCCESS, deserialized.getRights());
            deserialized.init();
        }, worldExecutorService)
                .whenComplete((aVoid, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Event
    private void playerDisconnectEvent(ClientDisconnectEvent c) {
        if (p == null || !p.getClient().isDisconnected()) {
            return;
        }


        players.put()
    }

    /**
     * Gets player by name.
     *
     * @param name the name
     * @return the player by name
     */
    public Optional<Player> getPlayerByName(String name) {
        return players.values().stream().filter((p) -> p.getUsername().equals(name)).findFirst();
    }

    /**
     * Gets player.
     *
     * @param index the index
     * @return the player
     */
    public Player getPlayer(int index) {
        return players.get(index);
    }

    /**
     * Gets total players.
     *
     * @return the total players
     */
    public int getTotalPlayers() {
        return playersCount - freePlayerSlots.size();
    }

    /**
     * Gets free slots.
     *
     * @return the free slots
     */
    public int getFreeSlots() {
        return players.size() - playersCount + freePlayerSlots.size();
    }

    /**
     * Poll.
     */
    private void poll() {
        /*
            Adds players that are currently in the loginQueue for this world to this worlds players arraylist.
         */
        handlePlayerDisconnects();
        doWorldTasks();
    }


    private void handlePlayerDisconnects() {
        for (int i = 0; i < players.length; i++) {
            int index = i;
            Player player = players[i];
            if (player != null && player.getClient().isDisconnected()) {
                Player.asyncPlayerStore().store(player).whenCompleteAsync((aBoolean, throwable) -> {
                    if (!aBoolean || throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }

                    //login entity saving
                    players.put()
                    freePlayerSlots.add(index);
                }, worldExecutorService);
            }
        }
    }

    private void doWorldTasks() {
        Task t;
        ArrayList<Task> incompleteTasks = new ArrayList<>();

        while ((t = worldTasks.poll()) != null) {
            if (!t.isFinished() && t.check()) {
                t.execute();
            }
            if (!t.isFinished()) {
                incompleteTasks.add(t);
            }
        }
        worldTasks.addAll(incompleteTasks);
    }


    /**
     * Queues a @class world.task.Task to be executed by the worlds main thread.
     * This should be used anytime world state is changed from a different thread,
     * and wont take a overly long time to execute.
     * <p>
     * Anything that will block, or loop for an extended period of time should not be put
     * on the world thread for execution, but rather executed asynchronously on another thread.
     * Final state changes, if any, can then be posted here.
     * <p>
     * <p>
     * This task works in unison with the main world thread.
     *
     * @param t the t
     */
    public void queueWorldTask(Task t) {
        worldTasks.add(t);
    }

    /**
     * Submits a task onto the world thread that is not in unison with main poll method.
     * <p>
     * The world thread runs @method poll every WorldConfig.WORLD_TICK_RATE_MS however this submitted task
     * will be run immediately even if poll has not been called.
     *
     * @param r the r
     * @return the future
     */
    public Future<?> submit(Runnable r) {
        return worldExecutorService.submit(r);
    }

    /**
     * Submit future.
     *
     * @param <T> the type parameter
     * @param r   the r
     * @return the future
     */
    public <T> Future<T> submit(Callable<T> r) {
        return worldExecutorService.submit(r);
    }

    private static class WorldThreadFactory implements ThreadFactory {
        private final int count = 0;
        private final int priority;

        /**
         * Instantiates a new World thread factory.
         *
         * @param priority the priority
         */
        public WorldThreadFactory(int priority) {

            if (priority > Thread.MAX_PRIORITY || priority < Thread.MIN_PRIORITY) {
                throw new InvalidStateException("Invalid priority for world thread.");
            }

            this.priority = priority;
        }


        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread();
            t.setName("World " + count + " thread");
            t.setPriority(priority);
            t.setUncaughtExceptionHandler((t1, e) -> e.printStackTrace());
            return t;
        }
    }
}
