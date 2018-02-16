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

package world.entity.player;

import database.CollectionAccessor;
import database.DatabaseConfig;
import database.serialization.GsonSerializer;
import net.impl.session.Client;
import sun.plugin.dom.exception.InvalidStateException;
import world.entity.Entity;
import world.entity.misc.Position;
import world.entity.player.containers.*;
import world.entity.update.player.PlayerUpdateBlock;
import world.entity.update.player.PlayerUpdateFlags;
import world.storage.AsyncPlayerStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The type Player.
 */
public class Player extends Entity {
    private static final AsyncPlayerStore asyncPlayerStore = new AsyncPlayerStore(
            new CollectionAccessor<>(new GsonSerializer<>(Player.class), "Evolution", DatabaseConfig.PLAYERS_COLLECTION,
                    Player.class));

    /**
     * Players local to this player
     * e.g they fall within 15 x and y in the coordinate space.

     */
    private final List<Player> localPlayers = new ArrayList<>();
    /**
     * The players skills
     */
    private final Skills skills = new Skills(this);
    /**
     * The players bank
     */
    private final Bank bank = new Bank(this);
    /**
     * The players inventory
     */
    private final Inventory inventory = new Inventory(this);
    /**
     * The players equipment
     */
    private final Equipment equipment = new Equipment(this);
    /**
     * The players update flags
     */
    private final PlayerUpdateFlags updateFlags = new PlayerUpdateFlags();
    /**
     * The players update block
     */
    private final PlayerUpdateBlock playerUpdateBlock = new PlayerUpdateBlock(this);
    /**
     * The client for the player, contains networking stuff.
     */
    private Client c;
    /**
     * The players username
     */
    private String username;
    /**
     * The players username
     */
    private String password;
    /**
     * The players username
     */
    private int rights;
    /**
     * The players username
     */
    private boolean isDisabled = false;


    /**
     * Instantiates a new Player.
     *
     * @param c the c
     */
    public Player(Client c) {
        this.c = c;
        c.setPlayer(this);
    }

    /**
     * Async player store async player store.
     *
     * @return the async player store
     */
    public static AsyncPlayerStore asyncPlayerStore() {
        return Player.asyncPlayerStore;
    }

    /**
     * Load completable future.
     *
     * @param p the p
     * @return the completable future
     */
    public static CompletableFuture<Optional<Player>> load(Player p) {
        return Player.asyncPlayerStore().load(p).thenApplyAsync(player -> Optional.ofNullable(player));
    }

    /**
     * Gets update flags.
     *
     * @return the update flags
     */
    public PlayerUpdateFlags getUpdateFlags() {
        return updateFlags;
    }


    /**
     * Gets skills.
     *
     * @return the skills
     */
    public Skills getSkills() {
        return skills;
    }

    /**
     * Gets player update block.
     *
     * @return the player update block
     */
    public PlayerUpdateBlock getPlayerUpdateBlock() {
        return playerUpdateBlock;
    }

    /**
     * Load completable future.
     *
     * @return the completable future
     */
    public CompletableFuture<Optional<Player>> load() {
        if (getUsername() == null) {
            throw new InvalidStateException("username must be set before loading player.");
        }
        return Player.asyncPlayerStore().load(this).thenApplyAsync(player -> Optional.ofNullable(player));
    }

    /**
     * Gets bank.
     *
     * @return the bank
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Get inventory inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get equipment equipment.
     *
     * @return the equipment
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Is disabled boolean.
     *
     * @return the boolean
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Sets disabled.
     *
     * @param disabled the disabled
     */
    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets rights.
     *
     * @return the rights
     */
    public int getRights() {
        return rights;
    }


    /**
     * Sets rights.
     *
     * @param rights the rights
     */
    public void setRights(int rights) {
        this.rights = rights;
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return c;
    }

    /**
     * Sets client.
     *
     * @param c the c
     */
    public void setClient(Client c) {
        this.c = c;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets local players.
     *
     * @return the local players
     */
    public List<Player> getLocalPlayers() {
        return localPlayers;
    }


    @Override
    public void poll() {
        getClient().getOutgoingPacketBuilder().playerUpdate().send();
    }
}
