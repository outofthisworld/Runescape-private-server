package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class ForcedChatUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
           /*builder.writeString(entity.getForcedChat());*/
    }
}
