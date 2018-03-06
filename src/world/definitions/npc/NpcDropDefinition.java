package world.definitions.npc;

import world.definitions.IDefinition;
import world.entity.npc.NpcDrop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * The type Npc drop definition.
 */
public class NpcDropDefinition implements IDefinition {
    private int id;
    private ArrayList<NpcDrop> drops = new ArrayList();

    public int getId() {
        return id;
    }

    /**
     * Gets drops immutable.
     *
     * @return the drops immutable
     */
    public List<NpcDrop> getDropsImmutable() {
        return Collections.unmodifiableList(drops);
    }

    /**
     * Gets drops mutable.
     *
     * @return the drops mutable
     */
    public List<NpcDrop> getDropsMutable() {
        return drops;
    }
}
