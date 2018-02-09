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
import net.Client;
import world.World;
import world.containers.Bank;
import world.containers.Equipment;
import world.containers.Inventory;
import world.event.Event;
import world.event.impl.PlayerLoginEvent;
import world.storage.AsyncPlayerStore;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The type Player.
 */
public class Player {
    private static final AsyncPlayerStore asyncPlayerStore = new AsyncPlayerStore(
            new CollectionAccessor<>("Players", "Evolution",
                    Player.class, fieldAttributes -> fieldAttributes.getName().equals("c")));
    private final int[] skills = new int[world.entity.player.Skill.values().length];
    private final int[] skillExp = new int[world.entity.player.Skill.values().length];
    private final Bank bank = new Bank(this);
    private final Inventory inventory = new Inventory(this);
    private final Equipment equipment = new Equipment(this);
    private int rights;
    private Client c;
    private String username;
    private String password;
    private boolean isDisabled = false;
    private int slotId;

    /**
     * Instantiates a new Player.
     */
    public Player() {

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
     * @param username the username
     * @return the completable future
     */
    public static CompletableFuture<Optional<Player>> load(String username) {
        return Player.asyncPlayerStore().load(username).thenApplyAsync(player -> Optional.ofNullable(player));
    }

    public int getSlotId() {
        return slotId;
    }

    private void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    @Event()
    private void playerLoginEvent(PlayerLoginEvent lEvent) {
        World loginWorld = lEvent.getWorld();
        int loginSlot = loginWorld.getSlot();

        if (loginSlot == -1) {

            //world full 7
            lEvent.getSender().sendResponse(7, 0, 0);
            return;
        }

        if (loginWorld.getPlayerByName(lEvent.getUsername()).isPresent()) {
            //already logged in 5

            lEvent.getSender().sendResponse(5, 0, 0);
            return;
        }

        Player.load(lEvent.getUsername()).cthenAcceptAsync(player -> {
            loginWorld.submit(() -> {
                Player decoded;

                if (player.isPresent()) {
                    decoded = player.get();
                    if (!decoded.getPassword().equals(decoded.getPassword())) {
                        decoded.getSender().sendResponse(3, 0, 0);
                        return;
                    }

                    if (decoded.isDisabled()) {
                        decoded.getSender().sendReponse(4, 0, 0);
                        return;
                    }
                } else {
                    decoded = new Player();
                    decoded.setUsername(decoded.getUsername());
                    decoded.setPassword(decoded.getPassword());
                    decoded.setRights(0);
                    decoded.asyncPlayerStore().store(decoded.getUsername(), decoded);
                }

                decoded.setClient(decoded.getClient());
                decoded.setSlotId(loginSlot);
                loginWorld.add(loginSlot, decoded);
            });
        });
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
    private void setClient(Client c) {
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
     * Gets skill level.
     *
     * @param skillId the skill id
     * @return the skill level
     */
    public int getSkillLevel(int skillId) {
        if (skillId < 0 || skillId >= skills.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }

        return skills[skillId];
    }


    /**
     * Gets skill level.
     *
     * @param skill the skill
     * @return the skill level
     */
    public int getSkillLevel(Skill skill) {
        return skills[skill.ordinal()];
    }


    /**
     * Gets skill exp.
     *
     * @param skill the skill
     * @return the skill exp
     */
    public int getSkillExp(Skill skill) {
        return skillExp[skill.ordinal()];
    }


    /**
     * Gets skill exp.
     *
     * @param skillId the skill id
     * @return the skill exp
     */
    public int getSkillExp(int skillId) {
        if (skillId < 0 || skillId >= skillExp.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }

        return skillExp[skillId];
    }

    /**
     * Sets skill level.
     *
     * @param skill      the skill
     * @param skillLevel the skill level
     */
    public void setSkillLevel(Skill skill, int skillLevel) {
        if (skillLevel < 1 || skillLevel > 99) {
            throw new IllegalArgumentException("Skill level must be between one and 99");
        }

        skills[skill.ordinal()] = skillLevel;
        skillExp[skill.ordinal()] = world.entity.player.Skill.getExpFromLevel(skillLevel);
        //c.getOutgoingPacketBuilder().updateSkill(skill.ordinal(), skills[skill.ordinal()], skillExp[skill.ordinal()]);
    }

    /**
     * Sets skill level.
     *
     * @param skillId    the skill id
     * @param skillLevel the skill level
     */
    public void setSkillLevel(int skillId, int skillLevel) {
        if (skillId < 0 || skillId >= skills.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }

        if (skillLevel < 1 || skillLevel > 99) {
            throw new IllegalArgumentException("Skill level must be between one and 99");
        }


        skills[skillId] = skillLevel;
        skillExp[skillId] = world.entity.player.Skill.getExpFromLevel(skillLevel);
        //c.getOutgoingPacketBuilder().updateSkill(skillId, skills[skillId], skillExp[skillId]);
    }

    /**
     * Sets skill exp.
     *
     * @param skill the skill
     * @param exp   the exp
     */
    public void setSkillExp(Skill skill, int exp) {
        skills[skill.ordinal()] = world.entity.player.Skill.getLevelFromExp(exp);
        skillExp[skill.ordinal()] = exp;
        //c.getOutgoingPacketBuilder().updateSkill(skill.ordinal(), skills[skill.ordinal()], skillExp[skill.ordinal()]);
    }

    /**
     * Sets skill exp.
     *
     * @param skillId the skill id
     * @param exp     the exp
     */
    public void setSkillExp(int skillId, int exp) {
        if (skillId < 0 || skillId >= skillExp.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }

        skills[skillId] = world.entity.player.Skill.getLevelFromExp(exp);
        skillExp[skillId] = exp;
        //c.getOutgoingPacketBuilder().updateSkill(skillId, skills[skillId], skillExp[skillId]);
    }

}
