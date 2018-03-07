package world.entity.combat.magic.spells;

import world.entity.combat.magic.Spell;
import world.entity.combat.magic.SpellType;
import world.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Abstract spell.
 */
public class AbstractSpell {
    private int spellId;
    private Spell spell;
    private SpellType spellType;
    private int baseExperience;
    private int requiredLevel;
    private int animation;
    private List<Item> itemsRequired = new ArrayList<>();
    private List<Item> equipmentRequired = new ArrayList<>();


    /**
     * Gets spell id.
     *
     * @return the spell id
     */
    public int getSpellId() {
        return spellId;
    }

    /**
     * Gets spell name.
     *
     * @return the spell name
     */
    public Spell getSpell() {
        return spell;
    }

    /**
     * Gets spell type.
     *
     * @return the spell type
     */
    public SpellType getSpellType() {
        return spellType;
    }

    /**
     * Gets base experience.
     *
     * @return the base experience
     */
    public int getBaseExperience() {
        return baseExperience;
    }

    /**
     * Gets required level.
     *
     * @return the required level
     */
    public int getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * Gets animation.
     *
     * @return the animation
     */
    public int getAnimation() {
        return animation;
    }

    /**
     * Gets items required.
     *
     * @return the items required
     */
    public List<Item> getItemsRequired() {
        return itemsRequired;
    }

    /**
     * Gets equipment required.
     *
     * @return the equipment required
     */
    public List<Item> getEquipmentRequired() {
        return equipmentRequired;
    }
}
