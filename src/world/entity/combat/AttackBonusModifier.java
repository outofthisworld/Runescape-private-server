package world.entity.combat;

public enum AttackBonusModifier {
    ATTACK_RANGED(1),
    ATTACK_SLASH(1),
    ATTACK_CRUSH(1),
    ATTACK_STAB(1);

    private int bonus;
    AttackBonusModifier(int bonus){
        this.bonus = bonus;
    }

    public int getBonus(){
        return bonus;
    }
}
