package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class AnimationUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
        //Animation animation = entity.getAnimation();
        //outputBuffer.writeShort(animation.getId(), ByteOrder.LITTLE).write(animation.getDelay(), ByteModification.NEGATION);
    }
}
