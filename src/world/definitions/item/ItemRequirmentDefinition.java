package world.definitions.item;


import world.definitions.IDefinition;
import world.definitions.types.ItemRequirement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 {
    "id": 4097,
    "requirements": [
      {
        "level": 20,
        "skill": "DEFENCE"
      },
      {
        "level": 40,
        "skill": "MAGIC"
      }
    ]
  },
 */
public class ItemRequirmentDefinition implements IDefinition {
    /*
        The item id of this requirement.
    */
    private int id;
    private final ArrayList<ItemRequirement> requirements = new ArrayList();

    public int getId() {
        return id;
    }

    /**
     * Returns an unmodifiable list of requirements.
     * @return
     */
    public List<ItemRequirement> getRequirementsImmutable() {
        return Collections.unmodifiableList(requirements);
    }

    /**
     * Returns an unmodifiable list of requirements.
     * @return
     */
    public List<ItemRequirement> getRequirementsMutable() {
        return requirements;
    }
}
