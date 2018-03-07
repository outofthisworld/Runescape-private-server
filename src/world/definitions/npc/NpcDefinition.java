package world.definitions.npc;


import world.definitions.IDefinition;


/**
 * The type NpcDefinition definition.
 */
public final class NpcDefinition implements IDefinition {
    private int id;
    private String name;
    private String examine;
    private int combat;
    private int size;
    private boolean attackable;
    private boolean agressive;
    private boolean retreats;
    private boolean poisonous;
    private int respawn;
    private int maxhit;
    private int hitpoints;
    private int attackSpeed;
    private int attackAnim;
    private int defenceAnim;
    private int deathAnim;
    private int attackBonus;
    private int defenceMelee;
    private int defenceRange;
    private int defenceMage;

    @Override
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets examine.
     *
     * @return the examine
     */
    public String getExamine() {
        return examine;
    }

    /**
     * Gets combat.
     *
     * @return the combat
     */
    public int getCombat() {
        return combat;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Is attackable boolean.
     *
     * @return the boolean
     */
    public boolean isAttackable() {
        return attackable;
    }

    /**
     * Is agressive boolean.
     *
     * @return the boolean
     */
    public boolean isAgressive() {
        return agressive;
    }

    /**
     * Is retreats boolean.
     *
     * @return the boolean
     */
    public boolean doesRetreat() {
        return retreats;
    }

    /**
     * Is poisonous boolean.
     *
     * @return the boolean
     */
    public boolean isPoisonous() {
        return poisonous;
    }

    /**
     * Gets respawn.
     *
     * @return the respawn
     */
    public int getRespawn() {
        return respawn;
    }

    /**
     * Gets maxhit.
     *
     * @return the maxhit
     */
    public int getMaxhit() {
        return maxhit;
    }

    /**
     * Gets hitpoints.
     *
     * @return the hitpoints
     */
    public int getHitpoints() {
        return hitpoints;
    }

    /**
     * Gets attack speed.
     *
     * @return the attack speed
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Gets attack anim.
     *
     * @return the attack anim
     */
    public int getAttackAnim() {
        return attackAnim;
    }

    /**
     * Gets defence anim.
     *
     * @return the defence anim
     */
    public int getDefenceAnim() {
        return defenceAnim;
    }

    /**
     * Gets death anim.
     *
     * @return the death anim
     */
    public int getDeathAnim() {
        return deathAnim;
    }

    /**
     * Gets attack bonus.
     *
     * @return the attack bonus
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * Gets defence melee.
     *
     * @return the defence melee
     */
    public int getDefenceMelee() {
        return defenceMelee;
    }

    /**
     * Gets defence range.
     *
     * @return the defence range
     */
    public int getDefenceRange() {
        return defenceRange;
    }

    /**
     * Gets defence mage.
     *
     * @return the defence mage
     */
    public int getDefenceMage() {
        return defenceMage;
    }

}