package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class ChatUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
              /*ChatMessage msg = entity.getChatMessage();
                        byte[] bytes = msg.getText();

                        writer.writeShort(((msg.getColor() & 0xFF) << 8) + (msg.getEffect() & 0xFF), ByteOrder.LITTLE)
                                .write(entity.getRights().getProtocolValue()).write(bytes.length, ByteModification.NEGATION)
                                .writeBytesReverse(bytes);*/
    }
}
