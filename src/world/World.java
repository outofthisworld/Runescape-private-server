package world;

import net.impl.decoder.Decoders;
import net.impl.decoder.LoginProtocolConstants;
import net.impl.decoder.LoginSessionDecoder;
import util.integrity.Preconditions;
import util.time.Stopwatch;
import world.definitions.DefinitionLoader;
import world.definitions.npc.NpcSpawnDefinition;
import world.entity.Entity;
import world.entity.npc.Npc;
import world.entity.player.Player;
import world.entity.player.update.PlayerUpdateBlock;
import world.entity.update.PlayerUpdateBlockCache;
import world.event.Event;
import world.event.EventBus;
import world.event.WorldEventBus;
import world.event.impl.ClientDisconnectEvent;
import world.event.impl.PlayerLoginEvent;
import world.event.impl.RegionUpdateEvent;
import world.region.RegionDivision;
import world.storage.SimpleCache;
import world.task.DefaultThreadFactory;
import world.task.Task;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * The type World.
 */
public class World {
    /**
     * The World executor service.
     */
    private final ScheduledExecutorService worldExecutorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(10));
    /**
     * The player update block cache. Used to cache player update blocks
     * to avoid reconstructing the block multiple times for different players.
     */
    private final SimpleCache<String, PlayerUpdateBlock> playerUpdateBlockCache = new PlayerUpdateBlockCache();
    /**
     * Players divided by region.
     */
    private final RegionDivision<Player> playerRegionDivision = new RegionDivision<>();
    /**
     * Npcs divided by region.
     */
    private final RegionDivision<Npc> npcRegionDivision = new RegionDivision<>();
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
     * The players in this world.
     */
    private EntityContainer<Player> playersInWorld = new EntityContainer<>(WorldConfig.MAX_PLAYERS_IN_WORLD);
    /**
     * The npcs in this world
     */
    private EntityContainer<Npc> npcsInWorld = new EntityContainer<>(WorldConfig.MAX_NPCS_IN_WORLD);
    /**
     * The worlds execution task
     */
    private ScheduledFuture<?> worldExecutionTask;


    /**
     * Instantiates a new World.
     *
     * @param worldId the world id
     */
    World(int worldId) {
        this.worldId = worldId;
        getEventBus().register(this);
    }

    private void loadNpcs() {
        Map<Integer, NpcSpawnDefinition> m = DefinitionLoader.getDefinitionMap(DefinitionLoader.NPC_SPAWNS);
        int[] count = {0};
        m.forEach((npcId, spawnDef) -> {
            npcsInWorld.add(new Npc(npcId, worldId, spawnDef.getPosition()));
        });
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
     * Responsible for loading everything to do with the world.
     * <p>
     * Called when all definitions have been loaded and all data is available.
     */
    public void load() {
        loadNpcs();
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
        if (worldExecutionTask != null)
            worldExecutionTask.cancel(true);
        worldTasks.clear();
    }

    public Collection<Player> getPlayers(){
        return playersInWorld.getItemsImmutable();
    }

    public Collection<Npc> getNpcs(){
        return npcsInWorld.getItemsImmutable();
    }


    /**
     * Gets player region division.
     *
     * @return the player region division
     */
    public RegionDivision<Player> getPlayerRegionDivision() {
        return playerRegionDivision;
    }

    /**
     * Gets npc region division.
     *
     * @return the npc region division
     */
    public RegionDivision<Npc> getNpcRegionDivision() {
        return npcRegionDivision;
    }


    /**
     * @param entity
     * @param container
     * @param <T>
     * @return
     */
    private <T extends Entity> int addEntityToWorld(T entity, EntityContainer<T> container,RegionDivision<T> regionDivision) {
        if (entity.getSlotId() != -1) {
            throw new IllegalStateException("Player slot was not -1 when adding to world.");
        }
        int slot = container.add(entity);
        entity.setWorldId(this.worldId);
        regionDivision.updateEntityRegion(entity);
        return slot;
    }

    /**
     * @param entity
     * @param container
     * @param regionDivision
     * @param <T>
     * @return
     */
    private <T extends Entity> boolean removeEntityFromWorld(T entity, EntityContainer<T> container, RegionDivision<T> regionDivision) {
        Preconditions.notNull(entity, container, regionDivision);
        if (!container.remove(entity)) {
            return false;
        }


        regionDivision.removeEntityFromRegion(entity);
        return true;
    }

    /**
     * Add player to world int.
     *
     * @param p the p
     * @return the int
     */
    public int addPlayerToWorld(Player p) {
        return addEntityToWorld(p, playersInWorld,playerRegionDivision);
    }

    /**
     * Add npc to world int.
     *
     * @param n the n
     * @return the int
     */
    public int addNpcToWorld(Npc n) {
        return addEntityToWorld(n, npcsInWorld,npcRegionDivision);
    }


    /**
     * Remove npc from world boolean.
     *
     * @param npc the npc
     * @return the boolean
     */
    public boolean removeNpcFromWorld(Npc npc) {
        Preconditions.notNull(npc);
        return removeEntityFromWorld(npc, npcsInWorld, npcRegionDivision);
    }

    /**
     * Remove npc from world boolean.
     *
     * @param slot the slot
     * @return the boolean
     */
    public boolean removeNpcFromWorld(int slot) {
        if (!npcsInWorld.contains(slot)) return false;
        return removeNpcFromWorld(npcsInWorld.get(slot));
    }

    /**
     * Remove player from world boolean.
     *
     * @param p the p
     * @return the boolean
     */
    public boolean removePlayerFromWorld(Player p) {
        removeEntityFromWorld(p, playersInWorld, playerRegionDivision);
        /*Player.asyncPlayerStore().store(p.getUsername(), p).whenCompleteAsync((aBoolean, throwable) -> {
            if (!aBoolean || throwable != null) {
                throwable.printStackTrace();
                return;
            }

            //login entity saving
            //players.put()
            freePlayerSlots.add(slot);
        }, worldExecutorService);*/
        return true;
    }

    /**
     * Add entity to world int.
     *
     * @param e the e
     * @return the int
     */
    public int addEntityToWorld(Entity e) {
        Preconditions.notNull(e);
        if (e.isPlayer()) {
            return addPlayerToWorld((Player) e);
        } else {
            return addNpcToWorld((Npc) e);
        }
    }

    /**
     * Remove entity from world boolean.
     *
     * @param e the e
     * @return the boolean
     */
    public boolean removeEntityFromWorld(Entity e) {
        Preconditions.notNull(e);
        if (e.isPlayer()) {
            return removePlayerFromWorld((Player) e);
        } else {
            return removeNpcFromWorld((Npc) e);
        }
    }

    /**
     * Remove player from world boolean.
     *
     * @param slot the slot
     * @return the boolean
     */
    public boolean removePlayerFromWorld(int slot) {
        if (!playersInWorld.contains(slot)) {
            return false;
        }

        Player p = playersInWorld.get(slot);
        return removePlayerFromWorld(p);
    }


    @Event()
    private void playerLoginEvent(PlayerLoginEvent lEvent) {

        if (lEvent.getDecoder() == null || lEvent.getClient() == null) {
            return;
        }

        LoginSessionDecoder lDecoder = lEvent.getSender();
        Player loginPlayer = new Player(lEvent.getUsername(), lEvent.getPassword(), lEvent.getClient(), worldId);

        if (getPlayerByName(loginPlayer.getUsername()).isPresent()) {
            lDecoder.sendResponse(lEvent.getClient(), LoginProtocolConstants.ALREADY_LOGGED_IN, 0);
            return;
        }

        if (playersInWorld.getRemaining() <= 0) {
            lDecoder.sendResponse(lEvent.getClient(), LoginProtocolConstants.WORLD_FULL, 0);
            return;
        }

        loginPlayer.load().thenAcceptAsync(player -> {
            Player loaded;

            boolean useDb = false;
            if (useDb && player.isPresent()) {
                loaded = player.get();
                if (!loaded.getPassword().equals(lEvent.getPassword())) {
                    lDecoder.sendResponse(lEvent.getClient(), LoginProtocolConstants.INVALID_USERNAME_OR_PASSWORD, 0);
                    return;
                }

                if (loaded.isDisabled()) {
                    lDecoder.sendResponse(lEvent.getClient(), LoginProtocolConstants.ACCOUNT_DISABLED, 0);
                    return;
                }
            } else {
                loaded = loginPlayer;
                util.integrity.Debug.writeLine("Creating new player" + loaded.getUsername());
                if (useDb) {
                    Player.asyncPlayerStore().store(loaded.getUsername(), loaded).whenComplete((aBoolean, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                    });
                }
            }

            int slot = addPlayerToWorld(loaded);
            if (slot == -1) {
                lEvent.getSender().sendResponse(lEvent.getClient(), LoginProtocolConstants.WORLD_FULL, 0);
                return;
            } else {
                loaded.getClient().setProtocolDecoder(Decoders.GAME_PACKET_DECODER);
                lEvent.getSender().sendResponse(lEvent.getClient(), LoginProtocolConstants.LOGIN_SUCCESS, loaded.getRights());
                loaded.init();
            }
        }, worldExecutorService).whenComplete((aVoid, throwable) -> {
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
        return playersInWorld.getItemsImmutable().stream().filter((p) -> p.getUsername().equals(name)).findFirst();
    }

    /**
     * Gets player by name.
     *
     * @param predicate the predicate
     * @return the player by name
     */
    public Optional<Player> getPlayerByName(Predicate<Player> predicate) {
        Preconditions.notNull(predicate);
        return playersInWorld.getItemsImmutable().stream().filter(predicate).findFirst();
    }


    /**
     * Gets player.
     *
     * @param index the index
     * @return the player
     */
    public Player getPlayer(int index) {
        return playersInWorld.get(index);
    }

    /**
     * Poll.
     */
    private void poll() {
        loopTimer.restart();

        doWorldTasks();

        for (Player player : playersInWorld.getItemsImmutable()) {
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

        for (Npc npc : npcsInWorld.getItemsImmutable()) {
            npc.poll();
        }

        /*
           Clear the player update block cache.
        */
        playerUpdateBlockCache.clear();

        /*
            Calculate how long it takes for one update.
         */
        loopTimer.stop();
        // logger.log(Level.INFO, "Completed world poll in " + loopTimer.getTimePassed(TimeUnit.MILLISECONDS) + "ms");
    }

    public int getTotalPlayers() {
        return playersInWorld.size();
    }

    public int getTotalNpcs() {
        return npcsInWorld.size();
    }

    @Event
    private void handleRegionUpdate(RegionUpdateEvent regionUpdate) {
        Objects.requireNonNull(regionUpdate);
        Player p = regionUpdate.getPlayer();
        Objects.requireNonNull(p);
        // updatePlayerRegion(p);
        p.getClient().getOutgoingPacketBuilder().updateRegion();
        p.setRegionChanged(true);
    }


    private void doWorldTasks() {
        Task t;
        LinkedList<Task> incompleteTasks = new LinkedList<>();

        while ((t = worldTasks.poll()) != null) {
            if (!t.isFinished() && t.check()) {
                System.out.println("executing");
                t.execute();
            }
            if (!t.isFinished()) {
                incompleteTasks.addLast(t);
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
