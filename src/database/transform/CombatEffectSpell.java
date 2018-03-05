package database.transform;

public abstract class CombatEffectSpell extends CombatNormalSpell {
    public abstract void effect(CharacterNode cast, CharacterNode castOn);
}
