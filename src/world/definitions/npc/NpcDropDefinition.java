package world.definitions.npc;

import world.definitions.types.NpcDrop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NpcDropDefinition {
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
