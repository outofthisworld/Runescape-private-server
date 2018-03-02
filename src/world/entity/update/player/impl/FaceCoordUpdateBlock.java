package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class FaceCoordUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
              /*builder.writeShort((Integer) entity.getAttributes().get(Attributes.FACING_COORDINATE_X),
                                ByteModification.ADDITION, ByteOrder.LITTLE)
                                .writeShort((Integer) entity.getAttributes()
                                        .get(Attributes.FACING_COORDINATE_Y), ByteOrder.LITTLE);*/
    }
}
