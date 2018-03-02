package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class GraphicsUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
            /*Graphic graphic = entity.getGraphic();
                        outputBuffer.writeShort(graphic.getId(), ByteOrder.LITTLE);
                        outputBuffer.writeInt(graphic.getDelay() | graphic.getHeight());*/
    }
}
