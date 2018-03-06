package world.definitions.player;

import world.definitions.IDefinition;
import world.entity.player.combat.magic.CombatSpell;
import world.entity.player.combat.magic.TeleportSpell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type CombatSpell book definition.
 */
public class SpellBookDefinition implements IDefinition {
    private int spellBookId;
    private String spellBookName;
    private int interfaceId;
    private List<TeleportSpell> teleportSpells = new ArrayList();
    private List<CombatSpell> combatSpells = new ArrayList<>();

    /**
     * Gets spell book id.
     *
     * @return the spell book id
     */
    @Override
    public int getId() {
        return spellBookId;
    }

    /**
     * Gets spell book name.
     *
     * @return the spell book name
     */
    public String getSpellBookName() {
        return spellBookName;
    }

    /**
     * Gets interface id.
     *
     * @return the interface id
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets teleport spells.
     *
     * @return the teleport spells
     */
    public List<TeleportSpell> getTeleportSpells() {
        return teleportSpells;
    }

    /**
     * Gets teleport spells immutable.
     *
     * @return the teleport spells immutable
     */
    public List<TeleportSpell> getTeleportSpellsImmutable() {
        return Collections.unmodifiableList(teleportSpells);
    }

    /**
     * Gets combat spells.
     *
     * @return the combat spells
     */
    public List<CombatSpell> getCombatSpells() {
        return combatSpells;
    }

    /**
     * Gets combat spells immutable.
     *
     * @return the combat spells immutable
     */
    public List<CombatSpell> getCombatSpellsImmutable() {
        return Collections.unmodifiableList(combatSpells);
    }
}
