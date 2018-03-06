package world.entity.npc;

import world.definitions.DefinitionLoader;
import world.definitions.npc.NpcDefinition;
import world.definitions.npc.NpcDropDefinition;
import world.definitions.npc.NpcSpawnDefinition;
import world.entity.Entity;
import world.entity.area.Position;
import world.entity.area.Vector;

public class Npc extends Entity {
    private int id;

    public Npc(int npcId, int slotId, int worldId, Position position) {
        this(npcId, slotId, worldId, position.getVector());
    }

    public Npc(int npcId, int slotId, int worldId, Vector position) {
        this.id = npcId;
        this.weight = 0;
        this.slotId = slotId;
        this.worldId = worldId;
        this.position.setVector(position.copy());
    }

    public Npc(int npcId, int slotId, int worldId, int x, int y, int z) {
        this(npcId, slotId, worldId, new Vector(x, y, z));
    }

    public NpcDefinition getNpcDefinition() {
        return DefinitionLoader.getDefinition(DefinitionLoader.NPC_DEFINITIONS, id);
    }

    public NpcSpawnDefinition getSpawnDefinition() {
        return DefinitionLoader.getDefinition(DefinitionLoader.NPC_SPAWNS, id);
    }

    public NpcDropDefinition getNpcDropDefinition() {
        return DefinitionLoader.getDefinition(DefinitionLoader.NPC_DROPS, id);
    }

    /*
        Any updates for this entity.
    */
    public void poll() {

        //Npc updating
        //Npc movement
    }
}
