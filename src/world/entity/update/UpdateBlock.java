package world.entity.update;

import net.buffers.OutputBuffer;

public abstract class UpdateBlock<T extends IFlag> {
    public abstract OutputBuffer build(T updateFlags);
}
