package world.entity.npc;

import util.random.Chance;
import util.random.RandomUtils;
import world.combat.CombatHandler;
import world.definitions.DefinitionLoader;
import world.definitions.npc.NpcDefinition;
import world.definitions.npc.NpcDropDefinition;
import world.definitions.npc.NpcSpawnDefinition;
import world.entity.Entity;
import world.entity.area.Position;
import world.entity.area.Vector;
import world.entity.npc.update.NpcUpdateBlock;
import world.entity.npc.update.NpcUpdateFlags;

public class Npc extends Entity {
    private int id;
    private int hitpoints;
    private int respawnTimer = -1;
    private final NpcUpdateBlock updateBlock = new NpcUpdateBlock(this);
    private final NpcUpdateFlags updateFlags = new NpcUpdateFlags();

    public Npc(int npcId, int slotId, int worldId, Position position) {
        this(npcId, slotId, worldId, position.getVector());
    }

    public Npc(int npcId, int slotId, int worldId, Vector position) {
        this.id = npcId;
        this.weight = 0;
        this.slotId = slotId;
        this.worldId = worldId;
        this.position.setVector(position.copy());
        this.hitpoints = getNpcDefinition().getHitpoints();
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

    public boolean isDead(){
        return respawnTimer != -1;
    }

    public double getHealthPercentage(){
        return (hitpoints / getNpcDefinition().getHitpoints()) * 100;
    }

    private void doMovement(){
        if(!isDead() && getSpawnDefinition().shouldRandomWalk() && !getCombatHandler().isUnderAttack()){
            //30 percent chance that a npc will move given the above conditions
            if(Chance.chanceWithin(30)) {
                Position p = getSpawnDefinition().getNpcCircleArea().generateRandomPosition();
                getMovement().beginMovement();
                getMovement().stepTo(p.getVector().getX(), p.getVector().getY());
                getMovement().finishMovement();
            }
        }
    }

    private void handleRetreat(){
        if(!isDead() && getCombatHandler().isUnderAttack() && getNpcDefinition().doesRetreat() && getHealthPercentage() <= 15){

        }
    }

    private void handleRespawn(){
        if(hitpoints <= 0){
            respawnTimer = getNpcDefinition().getRespawn();
            getNpcDropDefinition().getDropsImmutable().stream().filter(e->e.getDropChance().shouldDrop()).forEach(drop->{
                //Gets the drop for this npc
                int amount = RandomUtils.randomIntBetween(drop.getMinimum(),drop.getMaximum());
                int id = drop.getId();

                //Add to ground items
            });
        }


        if(respawnTimer > 0){
            respawnTimer--;
            return;
        }

        if(respawnTimer == 0){
            respawnTimer = -1;
            hitpoints = getNpcDefinition().getHitpoints();
        }
    }

    private void handleAggression(){
        if(!isDead() && getNpcDefinition().isAgressive() && !getCombatHandler().isUnderAttack()){
            //Find local players and attack
            /**
             *
             * foreach localplayer
             *    find closest local player
             *    if local player is within getSpawnDefinition().getNpcCircleArea().contains(player.getPosition())
             *    attack the player.
             */
        }
    }

    public int getId() {
        return id;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getRespawnTimer() {
        return respawnTimer;
    }

    public NpcUpdateBlock getUpdateBlock() {
        return updateBlock;
    }

    public NpcUpdateFlags getUpdateFlags() {
        return updateFlags;
    }

    /*
            Any updates for this entity.
        */
    public void poll() {
        handleRespawn();
        doMovement();
        handleAggression();
        handleRetreat();



        //Npc updating
        //Npc movement

        updateFlags.clear();
    }
}
