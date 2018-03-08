package world.entity.combat;

import world.entity.Entity;
import world.entity.player.Player;

public class CombatHandler {
    private Entity e;
    private AttackHandler attackHandler;
    private boolean isUnderAttack;
    private boolean autoRetaliate;

    /**
     * The entity we are currently attacking
     */
    private Entity attackingEntity;

    public interface AttackHandler{
        boolean handleAttack(Entity attacking, Entity underAttack);
    }

    public CombatHandler(Entity entity){
        this.e = entity;
    }


    public boolean attack(Entity entity){

        if(isUnderAttack){
            return false;
        }
        boolean attacked = false;

        if(e.isPlayer()){
            AttackType type = CombatAssistant.determinePlayerAttackType((Player) e);
            //attacked = attackHandlerFactory.apply(type).handleAttack(e, entity);

            if(!attacked){
                this.attackingEntity = null;
                this.attackHandler = null;
            }else{
                this.attackingEntity = entity;
                this.attackHandler = attackHandler;
            }
        }

        return attacked;
    }


    public boolean isAttacking() {
        return attackingEntity != null;
    }

    public boolean isUnderAttack() {
        return isUnderAttack;
    }

    public Entity getAttackingEntity() {
        return attackingEntity;
    }

    protected void setUnderAttack(boolean underAttack){
        this.isUnderAttack = true;
    }

    public boolean shouldAutoRetaliate() {
        return autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }
}
