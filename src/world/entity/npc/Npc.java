package world.entity.npc;

import util.integrity.Preconditions;
import util.random.Chance;
import util.random.RandomUtils;
import world.definitions.DefinitionLoader;
import world.definitions.npc.NpcDefinition;
import world.definitions.npc.NpcDropDefinition;
import world.definitions.npc.NpcSpawnDefinition;
import world.entity.Entity;
import world.area.Area;
import world.area.Position;
import world.area.Vector;
import world.entity.npc.update.NpcUpdateBlock;
import world.entity.npc.update.NpcUpdateFlags;
import world.entity.npc.update.NpcUpdateMask;
import world.entity.player.Player;
import world.entity.update.IFlag;

import java.util.HashSet;
import java.util.Set;

public class Npc extends Entity {
    /**
     * The id of the npc.
     */
    private int id;
    /**
     * The current hitpoitns level of the npc
     */
    private int hitpoints;
    /**
     * The respawn timer, -1 if the npc is alive.
     */
    private int respawnTimer = -1;
    /**
    * Update flags for this npc
    **/
    private final NpcUpdateBlock updateBlock = new NpcUpdateBlock(this);
    /**
    * Update flags for this npc
    **/
    private final NpcUpdateFlags updateFlags = new NpcUpdateFlags();

    /**
    * The players local to this npc.
    * */
    private Set<Player> localPlayers = new HashSet<>();

    public Npc(int npcId, int slotId, int worldId, Position position) {
        this(npcId, slotId, worldId, position==null?null:position.getVector());
    }

    public Npc(int npcId, int slotId, int worldId, Vector position) {
        super(worldId,position == null?null:new Position(position.copy()));
        Preconditions.greaterThanOrEqualTo(npcId,0);
        this.id = npcId;
        this.weight = 0;
        this.slotId = slotId;
        this.hitpoints = getNpcDefinition().getHitpoints();
    }

    public Npc(int npcId,int worldId, Vector position) {
        this(npcId,-1,worldId,position);
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
        if(!isDead() && !getMovement().isMoving() && !getCombatHandler().isUnderAttack()){
            //30 percent chance that a npc will move given the above conditions
            if(Chance.chanceWithin(15)) {
                Position p = getSpawnDefinition().getNpcCircleArea().generateRandomPosition();
                walkTo(p.getVector().getX(), p.getVector().getY());
            }
        }
    }

    private void handleRetreat(){
        if(!isDead() && getCombatHandler().isUnderAttack() && getNpcDefinition().isRetreats() && getHealthPercentage() <= 15){
            Area.TwoDimensional.Circle c = getSpawnDefinition().getNpcCircleArea();
            c.setRadius(getSpawnDefinition().getWalkRadius());
            Position p = c.generateRandomPosition();
            walkTo(p.getVector().getX(),p.getVector().getY());
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
        if(!isDead() && getNpcDefinition().isAggressive() && !getCombatHandler().isAttacking()){
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

    public int getTicksTillRespawn() {
        return respawnTimer;
    }

    public NpcUpdateBlock getUpdateBlock() {
        return updateBlock;
    }

    public IFlag<NpcUpdateMask> getUpdateFlags() {
        return updateFlags;
    }

    public Set<Player> getLocalPlayers() {
        return localPlayers;
    }

    /*
                Any updates for this entity.
            */
    public void poll() {
        getMovement().poll();
        //handleRespawn();
        doMovement();
        //handleAggression();
        //handleRetreat();



        //Npc updating
        //Npc movement
        updateFlags.clear();
    }
}
