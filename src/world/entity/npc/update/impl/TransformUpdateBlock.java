package world.entity.npc.update.impl;

import net.buffers.OutputBuffer;
import world.entity.npc.Npc;

import java.util.function.BiConsumer;

public class TransformUpdateBlock implements BiConsumer<Npc, OutputBuffer> {
    @Override
    public void accept(Npc npc, OutputBuffer buffer) {
        buffer.writeLittleWordTypeA(-1); //npc.getTransform();
    }
}