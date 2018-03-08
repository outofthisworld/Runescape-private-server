package world;

import net.impl.decoder.GamePacketDecoder;
import net.impl.decoder.LoginProtocolConstants;
import util.integrity.Preconditions;
import util.time.Stopwatch;
import world.definitions.DefinitionLoader;
import world.definitions.npc.NpcSpawnDefinition;
import world.entity.area.Position;
import world.entity.npc.Npc;
import world.entity.player.Player;
import world.entity.player.update.PlayerUpdateBlock;
import world.entity.region.RegionDivision;
import world.entity.update.PlayerUpdateBlockCache;
import world.event.Event;
import world.event.EventBus;
import world.event.WorldEventBus;
import world.event.impl.ClientDisconnectEvent;
import world.event.impl.PlayerLoginEvent;
import world.event.impl.RegionUpdateEvent;
import world.storage.SimpleCache;
import world.task.DefaultThreadFactory;
import world.task.Task;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The type World.
 */
public class World {
    /**
     * The World executor service.
     */
    private final ScheduledExecutorService worldExecutorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(10));
    /**
     * The set of free slots, appended to when a player leaves the world.
     */
    private final HashSet<Integer> freePlayerSlots = new HashSet<>();
    /**
     * The player update block cache. Used to cache player update blocks
     * to avoid reconstructing the block multiple times for different players.
     */
    private final SimpleCache<String, PlayerUpdateBlock> playerUpdateBlockCache = new PlayerUpdateBlockCache();
    /**
     * All players in the world.
     */
    private final HashMap<Integer, Player> players = new HashMap(WorldConfig.MAX_PLAYERS_IN_WORLD);
    /**
     * All npcs in the world.
     */
    private final HashMap<Integer, Npc> npcs = new HashMap();
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

    private void loadNpcs() {
        Map<Integer,NpcSpawnDefinition> m = DefinitionLoader.getDefinitionMap(DefinitionLoader.NPC_SPAWNS);
        int[] count = {0};
        m.forEach((npcId, spawnDef) -> {
            npcs.put(count[0], new Npc(npcId, count[0], worldId, spawnDef.getPosition()));
            count[0]++;
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
     * Add npc to world.
     */
    public void addNpcToWorld()

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

        getPlayerRegionDivision().updateEntityRegion(p);
        players.put(slot, p);
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

        if (p == null) {
            System.out.println("Null playeer when removing");
            throw new IllegalStateException("null player in players list");
        }


        getPlayerRegionDivision().removeEntityFromRegion(p);
        players.remove(slot);
        freePlayerSlots.add(slot);

        /*Player.asyncPlayerStore().store(p.getUsername(), p).whenCompleteAsync((aBoolean, throwable) -> {
            if (!aBoolean || throwable != null) {
                throwable.printStackTrace();
                return;
            }

            //login entity saving
            //players.put()
            freePlayerSlots.add(slot);
        }, worldExecutorService);*/
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
        logger.log(Level.INFO, "NEW PLAYER LOG IN EVENT FIRING");

        if (lEvent.getPlayer() == null) {
            return;
        }

        int loginSlot = getSlot();
        logger.log(Level.INFO, "Found slot for player " + loginSlot);

        if (loginSlot == -1) {
            //world full 7
            logger.log(Level.INFO, "Sending world full response");
            lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.WORLD_FULL, 0);
            return;
        }

        if (getPlayerByName(lEvent.getPlayer().getUsername()).isPresent()) {
            logger.log(Level.INFO, "Sending player logged in response");
            //already logged in 5
            lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.ALREADY_LOGGED_IN, 0);
            return;
        }

        lEvent.getPlayer().load().thenAcceptAsync(player -> {
            Player deserialized;
            boolean useDb = false;
            if (useDb && player.isPresent()) {
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
                deserialized.setPassword(lEvent.getPassword());
                if (useDb) {
                    Player.asyncPlayerStore().store(deserialized.getUsername(), deserialized).whenComplete((aBoolean, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                    });
                }
            }

            deserialized.setSlotId(loginSlot);
            deserialized.setWorldId(worldId);
            deserialized.getClient().setProtocolDecoder(new GamePacketDecoder());
            lEvent.getSender().sendResponse(lEvent.getPlayer().getClient(), LoginProtocolConstants.LOGIN_SUCCESS, deserialized.getRights());
            deserialized.init();
            addPlayerToWorld(loginSlot, deserialized);
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
     * Gets player by name.
     *
     * @param predicate the predicate
     * @return the player by name
     */
    public Optional<Player> getPlayerByName(Predicate<Player> predicate) {
        Preconditions.notNull(predicate);
        return players.values().stream().filter(predicate).findFirst();
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

        doWorldTasks();

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

        /*for (Npc npc : npcs.values()) {


                Update movement
                Send update packet

           // npc.poll();
        }*/

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
