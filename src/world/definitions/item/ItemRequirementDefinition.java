package world.definitions.item;


import world.definitions.IDefinition;
import world.definitions.types.ItemRequirement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemRequirementDefinition implements IDefinition {
    private final ArrayList<ItemRequirement> requirements = new ArrayList();
    /*
        The item id of this requirement.
    */
    private int id;

    public int getId() {
        return id;
    }

    /**
     * Returns an unmodifiable list of requirements.
     *
     * @return
     */
    public List<ItemRequirement> getRequirementsImmutable() {
        return Collections.unmodifiableList(requirements);
    }

    /**
     * Returns an unmodifiable list of requirements.
     *
     * @return
     */
    public List<ItemRequirement> getRequirementsMutable() {
        return requirements;
    }
}
