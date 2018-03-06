package world.entity.player;

import database.CollectionAccessor;
import database.DatabaseConfig;
import database.serialization.GsonSerializer;
import database.serialization.impl.FieldToIdPolicy;
import database.serialization.impl.PlayerSkipFieldPolicy;
import net.impl.decoder.LoginProtocolConstants;
import net.impl.session.Client;
import net.packets.outgoing.OutgoingPacket;
import sun.plugin.dom.exception.InvalidStateException;
import util.FormatStrings;
import util.Preconditions;
import world.entity.Entity;
import world.entity.misc.Position;
import world.entity.player.appearance.Appearance;
import world.entity.player.combat.AttackBonusModifier;
import world.entity.player.combat.AttackStyle;
import world.entity.player.combat.CombatStyle;
import world.entity.player.containers.Bank;
import world.entity.player.containers.Equipment;
import world.entity.player.containers.Inventory;
import world.entity.player.skill.Skills;
import world.entity.player.update.PlayerUpdateBlock;
import world.entity.player.update.PlayerUpdateFlags;
import world.event.impl.AbstractEvent;
import world.event.impl.RegionUpdateEvent;
import world.interfaces.SidebarInterface;
import world.storage.AsyncPlayerStore;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * The type Player.
 */
public class Player extends Entity {
    private static final AsyncPlayerStore asyncPlayerStore = new AsyncPlayerStore(
            new CollectionAccessor<>(new GsonSerializer<>(Player.class)
                    .setFieldSkipPolicy(new PlayerSkipFieldPolicy())
                    .setNamingStategy(new FieldToIdPolicy("username")),
                    "Evolution", DatabaseConfig.PLAYERS_COLLECTION,
                    Player.class));

    /**
     * Players local to this player
     * e.g they fall within 15 x and y in the coordinate space.
     */
    private final HashSet<Player> localPlayers = new LinkedHashSet<>();
    /**
     * The players skills
     */
    private final Skills skills = new Skills(this);
    /*
            The players appearence.
         */
    private final Appearance appearance = new Appearance(this);
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
    private final boolean isInitialized = false;
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
     * The last region this player belonged to.
     */
    private Position lastRegionPosition = null;
    /**
     * The last region this player belonged to.
     */
    private boolean isTeleporting = false;
    /**
     * The last region this player belonged to.
     */
    private boolean regionChanged = false;
    /**
     * The players combat style.
     */
    private CombatStyle combatStyle = new CombatStyle(422, 5860, AttackStyle.ACCURATE, AttackBonusModifier.ATTACK_CRUSH);


    /**
     * Instantiates a new Player.
     *
     * @param c the c
     */
    public Player() {
        /*
            Set this players client
         */
        this.c = c;
        /*
            Register this player with the world event bus
        */
        getWorld().getEventBus().register(this);
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
        return Player.asyncPlayerStore().load(p).thenApplyAsync(Optional::ofNullable);
    }

    /**
     * Gets combat style.
     *
     * @return the combat style
     */
    public CombatStyle getCombatStyle() {
        return combatStyle;
    }

    /**
     * Sets combat style.
     *
     * @param combatStyle the combat style
     */
    public void setCombatStyle(CombatStyle combatStyle) {
        this.combatStyle = combatStyle;
    }

    /**
     * Gets appearance.
     *
     * @return the appearance
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Is teleporting boolean.
     *
     * @return the boolean
     */
    public boolean isTeleporting() {
        return isTeleporting;
    }

    /**
     * Sets teleporting.
     *
     * @param teleporting the teleporting
     */
    public void setTeleporting(boolean teleporting) {
        isTeleporting = teleporting;
    }

    /**
     * Is region changed boolean.
     *
     * @return the boolean
     */
    public boolean isRegionChanged() {
        return regionChanged;
    }

    /**
     * Sets region changed.
     *
     * @param regionChanged the region changed
     */
    public void setRegionChanged(boolean regionChanged) {
        this.regionChanged = regionChanged;
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
        return Player.asyncPlayerStore().load(this).thenApplyAsync(Optional::ofNullable);
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
        Preconditions.notNull(c);
        this.c = c;
                /*
            Set the client player to this player
        */
        c.setPlayer(this);
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
        Preconditions.notNull(username);
        Preconditions.areEqual(LoginProtocolConstants.VALID_USERNAME_PREDICATE.test(username), true);
        this.username = username;
    }

    /**
     * Gets local players.
     *
     * @return the local players
     */
    public Set<Player> getLocalPlayers() {
        return localPlayers;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    /**
     * Gets last region position.
     *
     * @return the last region position
     */
    public Position getLastRegionPosition() {
        return lastRegionPosition;
    }

    /**
     * Sets last region position.
     *
     * @param lastRegionPosition the last region position
     */
    public void setLastRegionPosition(Position lastRegionPosition) {
        this.lastRegionPosition = lastRegionPosition;
    }

    public void teleport(int x, int y, int z) {
        setTeleporting(true);
        //Update x,y,z
    }


    /**
     * Init.
     */
    public void init() {
        /*
            Do initial appearance update.
         */
        appearance.finishUpdateAppearance();

        //Updates this players region in the world and sends region update packet
        send(new RegionUpdateEvent(this, null));

        getClient().getOutgoingPacketBuilder().initPlayer(1, getSlotId());

        getClient().getOutgoingPacketBuilder().setChatOptions(0, 0, 0);

        //Refresh our inventory
        getInventory().syncAll();
        //Refresh our equipment
        getEquipment().syncAll();
        //Refresh our skills
        getSkills().syncAll();

        /*
            Sidebar interfaces
        */
        for (SidebarInterface i : SidebarInterface.values()) {
            getClient().getOutgoingPacketBuilder().setSidebarInterface(i.ordinal(), i.getInterfaceId());
        }

        getClient().getOutgoingPacketBuilder().setRunEnergy(getMovement().getRunEnergy());


        getClient().getOutgoingPacketBuilder().sendMessage(FormatStrings.welcomeMessage(this));
        getClient().getOutgoingPacketBuilder().sendMessage("Enjoy your time on the server!");
        getClient().getOutgoingPacketBuilder().sendMessage(FormatStrings.visitWebsite());

        /**
         Finally, send everything at once.
         */
        getClient().getOutgoingPacketBuilder().send();
    }

    /**
     * Send.
     *
     * @param p the p
     */
    public void send(OutgoingPacket p) {
        getClient().write(p);
    }

    /**
     * Send.
     *
     * @param e the e
     */
    public void send(AbstractEvent e) {
        getWorld().getEventBus().fire(e);
    }

    @Override
    public void poll() {

        /*
           Updates player movement
        */
        getMovement().poll();


        getClient().getOutgoingPacketBuilder().playerUpdate().send();

        regionChanged = false;
        isTeleporting = false;
        getUpdateFlags().clear();
    }
}
