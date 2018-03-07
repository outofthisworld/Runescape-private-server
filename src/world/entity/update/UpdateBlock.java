package world.entity.update;


import net.buffers.OutputBuffer;
import world.entity.npc.update.NpcUpdateBlock;

public abstract class UpdateBlock<T extends IFlag> {
    protected final OutputBuffer updateBlock;
    protected long mask;

    public UpdateBlock(int size, int increaseSize){
        this.updateBlock = OutputBuffer.create(size, increaseSize);
    }

    public abstract UpdateBlock build(T updateFlags);

    public abstract OutputBuffer getBlock();

    public long getMask(){
        return mask;
    }
}
