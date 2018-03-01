

package world.definitions.npc;


import world.definitions.IDefinition;


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

    public String getName() {
        return name;
    }

    public String getExamine() {
        return examine;
    }

    public int getCombat() {
        return combat;
    }

    public int getSize() {
        return size;
    }

    public boolean isAttackable() {
        return attackable;
    }

    public boolean isAgressive() {
        return agressive;
    }

    public boolean isRetreats() {
        return retreats;
    }

    public boolean isPoisonous() {
        return poisonous;
    }

    public int getRespawn() {
        return respawn;
    }

    public int getMaxhit() {
        return maxhit;
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

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDefenceMelee() {
        return defenceMelee;
    }

    public int getDefenceRange() {
        return defenceRange;
    }

    public int getDefenceMage() {
        return defenceMage;
    }
}