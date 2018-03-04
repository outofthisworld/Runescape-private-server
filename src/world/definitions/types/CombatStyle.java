package world.definitions.types;

public class CombatStyle{
    private AttackStyle style;
    private int animationId;
    private AttackBonusModifier bonusModifier;

    public AttackStyle getStyle() {
        return style;
    }

    public int getAnimationId() {
        return animationId;
    }

    public AttackBonusModifier getBonusModifier() {
        return bonusModifier;
    }
}