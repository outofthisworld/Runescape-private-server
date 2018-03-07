package world.definitions.player;

import util.integrity.Preconditions;
import world.definitions.IDefinition;
import world.entity.combat.magic.Spell;
import world.entity.combat.magic.SpellBook;
import world.entity.combat.magic.spells.AbstractSpell;
import world.entity.combat.magic.spells.CombatSpell;
import world.entity.combat.magic.spells.TeleportSpell;

import java.util.*;

/**
 * The type CombatSpell book definition.
 */
public class SpellBookDefinition implements IDefinition {
    private int spellBookId;
    private SpellBook spellBook;
    private int interfaceId;
    private List<TeleportSpell> teleportSpells = new ArrayList();
    private List<CombatSpell> combatSpells = new ArrayList<>();
    private Map<Integer, TeleportSpell> teleportSpellsHm = new HashMap<>();
    private Map<Integer, CombatSpell> combatSpellsHm = new HashMap<>();
    private boolean initialized = false;

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
     * Get spell abstract spell.
     *
     * @param spell the spell
     * @return the abstract spell
     */
    public AbstractSpell getSpell(Spell spell) {
        Preconditions.notNull(spell);
        CombatSpell cbSpell = combatSpellsHm.get(spell.getId());
        TeleportSpell telSpell = teleportSpellsHm.get(spell.getId());

        return cbSpell != null ? cbSpell : telSpell;
    }

    /**
     * Gets spell.
     *
     * @param spell the spell
     * @return the spell
     */
    public AbstractSpell getSpell(int spell) {
        return getSpell(Spell.fromIndex(spell));
    }

    /**
     * Get teleport spell teleport spell.
     *
     * @param spell the spell
     * @return the teleport spell
     */
    public TeleportSpell getTeleportSpell(Spell spell) {
        Preconditions.notNull(spell);
        return teleportSpellsHm.get(spell.getId());
    }

    /**
     * Gets teleport spell.
     *
     * @param spell the spell
     * @return the teleport spell
     */
    public TeleportSpell getTeleportSpell(int spell) {
        return teleportSpellsHm.get(Spell.fromIndex(spell));
    }

    /**
     * Get combat spell combat spell.
     *
     * @param spell the spell
     * @return the combat spell
     */
    public CombatSpell getCombatSpell(Spell spell) {
        Preconditions.notNull(spell);
        return combatSpells.get(spell.getId());
    }

    /**
     * Gets combat spell.
     *
     * @param spell the spell
     * @return the combat spell
     */
    public CombatSpell getCombatSpell(int spell) {
        return combatSpellsHm.get(Spell.fromIndex(spell));
    }

    /**
     * Gets spell book name.
     *
     * @return the spell book name
     */
    public SpellBook getSpellBook() {
        return spellBook;
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

    /**
     * Load.
     */
    public void load() {

        if (initialized) return;

        getCombatSpells().stream().forEach(s -> {
            if (combatSpellsHm.containsKey(s.getSpellId())) {
                throw new RuntimeException("Duplicate key error for combat spell " + s.getSpellId());
            }

            combatSpellsHm.put(s.getSpellId(), s);
        });

        getTeleportSpells().stream().forEach(s -> {
            if (teleportSpellsHm.containsKey(s.getSpellId())) {
                throw new RuntimeException("Duplicate key error for teleport spell " + s.getSpellId());
            }
            teleportSpellsHm.put(s.getSpellId(), s);
        });

        initialized = !initialized;
    }
}
