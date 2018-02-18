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
import util.Preconditions;
import util.Stopwatch;
import world.entity.misc.Position;
import world.entity.player.Player;
import world.entity.update.UpdateBlockCache;
import world.entity.update.player.PlayerUpdateBlock;
import world.event.Event;
import world.event.EventBus;
import world.event.WorldEventBus;
import world.event.impl.ClientDisconnectEvent;
import world.event.impl.PlayerLoginEvent;
import world.event.impl.RegionUpdateEvent;
import world.storage.SimpleCache;
import world.task.Task;
import world.task.WorldThreadFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type World.
 */
public class World {
    /**
     * The World executor service.
     */
    private final ScheduledExecutorService worldExecutorService = Executors.newSingleThreadScheduledExecutor(new WorldThreadFactory(10));
    /**
     * The set of free slots, appended to when a player leaves the world.
     */
    private final HashSet<Integer> freePlayerSlots = new HashSet<>();
    /**
     * The player update block cache. Used to cache player update blocks
     * to avoid reconstructing the block multiple times for different players.
     */
    private final SimpleCache<String, PlayerUpdateBlock> playerUpdateBlockCache = new UpdateBlockCache();
    /**
     * All players in the world.
     */
    private final HashMap<Integer, Player> players = new HashMap(WorldConfig.MAX_PLAYERS_IN_WORLD);
    /**
     * Divides players up by region, to make processing of certain things easier. E.G
     * AOE spells.
     */
    private final Map<Position, Set<Player>> playersByRegion = new HashMap<>();
    /**
     * Tasks queued to execute on the world thread.
     */
    private final ConcurrentLinkedQueue<Task> worldTasks = new ConcurrentLinkedQueue<>();
    /**
     * The world event bus. Any class can register events with this event bus
     * and also choose when to fire certain events. The events, in the case of @class WorldEventBus
     * are always fired on the world thread, but not in sync with the world. As the world
     * thread fires every WorldConfig.WORLD_TICK_RATE_MS, it is possible that an event will execute
     * during the period of time between a successive world tick.
     * <p>
     * E.G world thread executes @method poll();
     * poll takes 350ms to complete
     * 250ms left until next poll
     * event is fired during this 250ms wait between next execution.
     */
    private final EventBus eventBus = new WorldEventBus(this);
    /**
     * The class logger.
     */
    private final Logger logger = Logger.getLogger(World.class.getName());
    /**
     * A simple timer to time how long the main world loop takes.
     */
    private final Stopwatch loopTimer = new Stopwatch();
    /**
     * The worlds id.
     */
    private final int worldId;
    /**
     * The worlds execution task
     */
    private ScheduledFuture<?> worldExecutionTask;
    /**
     * The players count (not that this is not the direct players count and should never be
     * returned directly. See @method getPlayersCount();
     */
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
     * Gets update block cache.
     *
     * @return the update block cache
     */
    public SimpleCache<String, PlayerUpdateBlock> getPlayerUpdateBlockCache() {
        return playerUpdateBlockCache;
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
    public int getSlot() {

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
     * Is slot empty boolean.
     *
     * @param slot the slot
     * @return the boolean
     */
    public boolean isSlotEmpty(int slot) {
        if (slot < 0 || slot >= WorldConfig.MAX_PLAYERS_IN_WORLD) {
            return true;
        }
        return !players.containsKey(slot);
    }

    /**
     * Add.
     *
     * @param slot the slot
     * @param p    the p
     */
    public void addPlayerToWorld(int slot, Player p) {
        Preconditions.notNull(p);
        Preconditions.inRangeClosed(slot, 0, WorldConfig.MAX_PLAYERS_IN_WORLD);

        if (players.containsKey(slot)) {
            throw new IllegalArgumentException("Player slot was not null");
        }

        addPlayerToRegion(p);
        players.put(slot, p);
    }

    private void addPlayerToRegion(Player p) {
        Preconditions.notNull(p);

        Position currentPosition = p.getPosition();
        int regionX = currentPosition.getRegionX();
        int regionY = currentPosition.getRegionY();
        int z = currentPosition.getVector().getZ();
        Position regionPosition = new Position(regionX, regionY, z);
        addPlayerToRegion(p, regionPosition);
    }

    /**
     * Gets players by region.
     * The set returned is unmodifiable.
     */
    public Optional<Set<Player>> getPlayersByRegion(Position regionPosition) {
        if (!playersByRegion.containsKey(regionPosition)) {
            return Optional.empty();
        }

        return Optional.of(Collections.unmodifiableSet(playersByRegion.get(regionPosition)));
    }

    private void addPlayerToRegion(Player p, Position regionPosition) {
        Preconditions.notNull(p, regionPosition);

        if (!playersByRegion.containsKey(regionPosition)) {
            Set<Player> set = new HashSet<>();
            set.add(p);
            playersByRegion.put(regionPosition, set);
        } else {
            playersByRegion.get(regionPosition).add(p);
        }

        p.setLastRegionPosition(regionPosition);
    }

    /**
     * Update player region.
     * Should be called any time a players region is updated
     * which will in turn update the playersByRegion map.
     *
     * @param p the p
     */
    public void updatePlayerRegion(Player p) {
        Preconditions.notNull(p, p.getLastRegionPosition());

        Position lastRegionPosition = p.getLastRegionPosition();

        Preconditions.notNull(playersByRegion.get(lastRegionPosition));

        Set<Player> playersInRegion = playersByRegion.get(lastRegionPosition);

        Preconditions.areEqual(playersInRegion.contains(p), true);

        playersInRegion.remove(p);

        addPlayerToRegion(p);
    }

    /**
     * Add player to world.
     *
     * @param p the p
     */
    public void addPlayerToWorld(Player p) {
        addPlayerToWorld(p.getSlotId(), p);
    }

    /**
     * Remove player from world.
     *
     * @param slot the slot
     */
    public void removePlayerFromWorld(int slot) {
        Preconditions.inRangeClosed(slot, 0, WorldConfig.MAX_PLAYERS_IN_WORLD);

        if (isSlotEmpty(slot)) {
            return;
        }

        Player p = players.get(slot);

        Player.asyncPlayerStore().store(p.getUsername(), p).whenCompleteAsync((aBoolean, throwable) -> {
            if (!aBoolean || throwable != null) {
                throwable.printStackTrace();
                return;
            }

            //login entity saving
            //players.put()
            freePlayerSlots.add(slot);
        }, worldExecutorService);

    }

    /**
     * Remove player from world.
     *
     * @param p the p
     */
    public void removePlayerFromWorld(Player p) {
        Preconditions.notNull(p);
        removePlayerFromWorld(p.getSlotId());
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
                if (!deserialized.getPassword().equals(lEvent.getPassword())) {
                    lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.INVALID_USERNAME_OR_PASSWORD, 0);
                    return;
                }

                if (deserialized.isDisabled()) {
                    lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.ACCOUNT_DISABLED, 0);
                    return;
                }
            } else {
                deserialized = lEvent.getPlayer();
                deserialized.getAppearance().setDefault();
                Player.asyncPlayerStore().store(deserialized.getUsername(), deserialized);
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
        Player p = c.getSender().getPlayer();

        if (p == null || !p.getClient().isDisconnected()) {
            return;
        }

        removePlayerFromWorld(p);
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
        loopTimer.restart();
        for (Player player : players.values()) {
            if (player.getClient().isDisconnected()) {
                removePlayerFromWorld(player);
                continue;
            }

            /*
                Update movement
                Send update packet
            */
            player.poll();
        }

        doWorldTasks();
        /*
           Clear the player update block cache.
        */
        playerUpdateBlockCache.clear();
        loopTimer.stop();
        logger.log(Level.INFO, "Completed world poll in " + loopTimer.getTimePassed(TimeUnit.MILLISECONDS) + "ms");
    }

    @Event
    private void handleRegionUpdate(RegionUpdateEvent regionUpdate) {
        Objects.requireNonNull(regionUpdate);
        Player p = regionUpdate.getPlayer();
        Objects.requireNonNull(p);
        updatePlayerRegion(p);
        p.getClient().getOutgoingPacketBuilder().updateRegion().send();
        p.setRegionChanged(true);
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
}
