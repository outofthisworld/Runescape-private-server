package world.entity.npc.update;

import net.buffers.ByteTransformationType;
import net.buffers.Order;
import net.buffers.OutputBuffer;
import util.integrity.Preconditions;
import world.entity.npc.Npc;
import world.entity.npc.update.impl.*;
import world.entity.update.IFlag;
import world.entity.update.UpdateBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class NpcUpdateBlock extends UpdateBlock<IFlag<NpcUpdateMask>>{
    private final Npc npc;

    public NpcUpdateBlock(Npc npc){
        super(2048,1024);
        Preconditions.notNull(npc);
        this.npc = npc;
    }

    private final static Map<NpcUpdateMask,BiConsumer<Npc, OutputBuffer>> flagMap = new HashMap<NpcUpdateMask,BiConsumer<Npc, OutputBuffer>>(){
        {
            put(NpcUpdateMask.FACE_CHARACTER,new EntityInteractionUpdateBlock());
            put(NpcUpdateMask.ANIMATION,new AnimationUpdateBlock());
            put(NpcUpdateMask.GRAPHICS,new GraphicsUpdateBlock());
            put(NpcUpdateMask.SINGLE_HIT,new SingleHitUpdateBlock());
            put(NpcUpdateMask.DOUBLE_HIT, new DoubleHitUpdateBlock());
            put(NpcUpdateMask.FACE_COORDINATE,new FaceCoordinateUpdateBlock());
            put(NpcUpdateMask.TRANSFORM,new TransformUpdateBlock());
            put(NpcUpdateMask.FORCED_CHAT,new ForcedChatUpdateBlock());
        }
    };

    @Override
    public UpdateBlock build(IFlag<NpcUpdateMask> updateFlags) {

        this.mask = updateFlags.getMask();
           /*
            Clear the current update block.
        */
        updateBlock.clear();

        /*
            Write the mask as little endian.
        */
        updateBlock.order(Order.LITTLE_ENDIAN);

        long mask = updateFlags.getMask();
        if (updateFlags.getMask() >= 0x100L) {
            updateBlock.writeBytes(mask, 2, ByteTransformationType.NONE);
        } else {
            updateBlock.writeByte((int) mask);
        }

        updateBlock.order(Order.BIG_ENDIAN);


        for (NpcUpdateMask m : NpcUpdateMask.values()) {
            if (updateFlags.isSet(m)) {
                flagMap.get(m).accept(npc, updateBlock);
            }
        }
        return this;
    }

    @Override
    public OutputBuffer getBlock() {
        return updateBlock;
    }

    @Override
    public long getMask() {
        return mask;
    }
}
