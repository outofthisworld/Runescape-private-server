package world.entity.update;


import net.buffers.OutputBuffer;

public abstract class UpdateBlock<T extends IFlag> {
    public abstract UpdateBlock build(T updateFlags);

    public abstract OutputBuffer getBlock();

    public abstract long getMask();
}
