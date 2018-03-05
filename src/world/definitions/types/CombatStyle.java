package world.definitions.types;

public class CombatStyle {
    private AttackStyle style;
    private int animationId;
    private int gameButtonId;
    private AttackBonusModifier bonusModifier;

    public CombatStyle() {
    }

    public CombatStyle(int animationId, int gameButtonId, AttackStyle style, AttackBonusModifier bonusModifier) {
        this.animationId = animationId;
        this.style = style;
        this.bonusModifier = bonusModifier;
        this.gameButtonId = gameButtonId;
    }

    public int getGameButtonId() {
        return gameButtonId;
    }

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