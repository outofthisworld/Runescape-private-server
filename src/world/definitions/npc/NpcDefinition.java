package world.definitions.npc;


import world.definitions.IDefinition;


/**
 * The type NpcDefinition definition.
 */
public final class NpcDefinition implements IDefinition {
     private int id;
     private String name;
     private String examine;
     private int size;
     private int walkRadius;
     private boolean attackable;
     private boolean retreats;
     private boolean aggressive;
     private int respawn;
     private int maxHit;
     private int hitpoints;
     private int attackSpeed;
     private int attackAnim;
     private int defenceAnim;
     private int deathAnim;
     private int combatLevel;
     private int[] stats = new int[18];
     private int slayerLevel;
     private int combatFollowDistance;
     private String[] combatTypes = new String[6];

    @Override
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

    public int getRespawn() {
        return respawn;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public int getHitpoints() {
        return hitpoints;
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