package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class ForceMovementUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
             /*ForceMovement movement = entity.getForceMovement();

                        writer.write(movement.getStartLocation().getX(), ByteModification.ADDITION)
                                .write(movement.getStartLocation().getY(), ByteModification.NEGATION)
                                .write(movement.getEndLocation().getX(), ByteModification.SUBTRACTION).write(movement.getEndLocation().getY())
                                .writeShort(movement.getDurationX()).write(movement.getDurationY(), ByteModification.ADDITION)
                                .write(movement.getWalkDirection().getId());*/
    }
}
