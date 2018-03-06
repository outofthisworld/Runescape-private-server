package world.definitions.item;

import world.definitions.IDefinition;

/**
 * The type Weapon poison definition.
 */
public class WeaponPoisonDefinition implements IDefinition {
    private int id;
    private String type;

    public int getId() {
        return id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }
}
