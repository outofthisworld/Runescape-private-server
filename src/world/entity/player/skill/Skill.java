package world.entity.player.skill;


import java.util.HashMap;

/**
 * The enum Skill.
 */
public enum Skill {
    /**
     * The constant ATTACK.
     */
    ATTACK,
    /**
     * The constant DEFENCE.
     */
    DEFENCE,
    /**
     * The constant STRENGTH.
     */
    STRENGTH,
    /**
     * The constant HITPOINTS.
     */
    HITPOINTS,
    /**
     * The constant RANGED.
     */
    RANGED,
    /**
     * The constant PRAYER.
     */
    PRAYER,
    /**
     * The constant MAGIC.
     */
    MAGIC,
    /**
     * The constant COOKING.
     */
    COOKING,
    /**
     * The constant WOODCUTTING.
     */
    WOODCUTTING,
    /**
     * The constant FLETCHING.
     */
    FLETCHING,
    /**
     * The constant FISHING.
     */
    FISHING,
    /**
     * The constant FIREMAKING.
     */
    FIREMAKING,
    /**
     * The constant CRAFTING.
     */
    CRAFTING,
    /**
     * The constant SMITHING.
     */
    SMITHING,
    /**
     * The constant MINING.
     */
    MINING,
    /**
     * The constant HERBLORE.
     */
    HERBLORE,
    /**
     * The constant AGILITY.
     */
    AGILITY,
    /**
     * The constant THIEVING.
     */
    THIEVING,
    /**
     * The constant SLAYER.
     */
    SLAYER,
    /**
     * The constant FARMING.
     */
    FARMING,
    /**
     * The constant RUNECRAFTING.
     */
    RUNECRAFTING,
    /**
     * The constant CONSTRUCTION.
     */
    CONSTRUCTION,
    /**
     * The constant HUNTING.
     */
    HUNTING;

    private static final HashMap<Integer, Skill> hm;

    static {
        Skill[] skills = Skill.values();
        hm = new HashMap<>(skills.length);
        for (int i = 0; i < skills.length; i++) {
            hm.put(i, skills[i]);
        }
    }

    /**
     * Gets exp from level.
     *
     * @param level the level
     * @return the exp from level
     */
    public static int getExpFromLevel(int level) {
        int points = 0;
        int output = 0;

        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    /**
     * Gets level from exp.
     *
     * @param currentExp the current exp
     * @return the level from exp
     */
    public static int getLevelFromExp(int currentExp) {
        int exp = currentExp;
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl < 100; lvl++) {
            points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if ((output - 1) >= exp) {
                return lvl;
            }
        }
        return 99;
    }

    public static Skill fromIndex(int i) {
        return hm.get(i);
    }
}

