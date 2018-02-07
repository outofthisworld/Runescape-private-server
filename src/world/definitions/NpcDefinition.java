package world.definitions;

import database.CollectionAccessor;
import database.IDBAccessor;

import java.util.Collections;
import java.util.List;

public final class NpcDefinition {
    private static final IDBAccessor<NpcDefinition> npcDB = new CollectionAccessor<>("Items", NpcDefinition.class);
    private static List<NpcDefinition> npcsDefinitions = null;
    private final boolean attackable = false;
    private final boolean retreats = false;
    private final boolean aggressive = false;
    private final boolean poisinous = false;
    private final int respawn = 20;
    private final int maxHit = 0;
    private final int attackSpeed = 6;
    private final int[] stats = new int[18];
    private final String[] combatTypes = new String[6];
    private int id;
    private String name;
    private String examine;
    private int size;
    private int walkRadius;
    private int hitpoints;
    private int attackAnim;
    private int defenceAnim;
    private int deathAnim;
    private int combatLevel;
    private int slayerLevel;
    private int combatFollowDistance;

    private NpcDefinition() {
    }

    public static NpcDefinition getForId(int id) {
        if (NpcDefinition.npcsDefinitions == null || id < 0 || id >= NpcDefinition.npcsDefinitions.size()) {
            return null;
        }

        return NpcDefinition.npcsDefinitions.get(id);
    }

    public static void load() {
        if (NpcDefinition.npcsDefinitions != null) {
            return;
        }
        NpcDefinition.npcsDefinitions = Collections.unmodifiableList(NpcDefinition.npcDB.findAll());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExamine() {
        return examine;
    }

    public int getSize() {
        return size;
    }

    public int getWalkRadius() {
        return walkRadius;
    }

    public boolean isAttackable() {
        return attackable;
    }

    public boolean isRetreats() {
        return retreats;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public boolean isPoisinous() {
        return poisinous;
    }

    public int getRespawn() {
        return respawn;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getAttackAnim() {
        return attackAnim;
    }

    public int getDefenceAnim() {
        return defenceAnim;
    }

    public int getDeathAnim() {
        return deathAnim;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public int[] getStats() {
        return stats;
    }

    public int getSlayerLevel() {
        return slayerLevel;
    }

    public int getCombatFollowDistance() {
        return combatFollowDistance;
    }

    public String[] getCombatTypes() {
        return combatTypes;
    }
}