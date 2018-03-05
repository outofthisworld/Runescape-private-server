package world.definitions.npc;

import world.definitions.IDefinition;
import world.entity.npc.NpcDrop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NpcDropDefinition implements IDefinition {
    private int id;
    private ArrayList<NpcDrop> drops = new ArrayList();

    public int getId() {
        return id;
    }

    public List<NpcDrop> getDropsImmutable() {
        return Collections.unmodifiableList(drops);
    }

    public List<NpcDrop> getDropsMutable() {
        return drops;
    }
}
