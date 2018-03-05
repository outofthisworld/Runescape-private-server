package database.transform;

public abstract class CombatAncientSpell extends CombatNormalSpell {
    public abstract int radius();

    public abstract void effect(CharacterNode cast, CharacterNode castOn, int damage);
}
