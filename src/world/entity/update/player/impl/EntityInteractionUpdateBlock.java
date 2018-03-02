package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class EntityInteractionUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
             /*Entity entity = target.getInteractingEntity();

                        if (entity != null) {
                            int index = entity.getSlot();

                            if (entity instanceof Player) {
                                index += +32768;
                            }

                            builder.writeShort(index, ByteOrder.LITTLE);
                        } else {
                            builder.writeShort(-1, ByteOrder.LITTLE);
                        }*/
    }
}
