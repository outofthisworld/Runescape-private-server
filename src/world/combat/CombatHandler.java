package world.combat;

import world.entity.Entity;

public class CombatHandler {
    private Entity entity;
    private boolean isAttacking;
    private boolean isUnderAttack;

    public CombatHandler(Entity entity){
        this.entity = entity;
    }

    public void attack(Entity entity){
        isAttacking = true;
        entity.getCombatHandler().setUnderAttack(true);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isUnderAttack() {
        return isUnderAttack;
    }

    protected void setUnderAttack(boolean underAttack){
        this.isUnderAttack = true;
    }
}
