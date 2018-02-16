package world.entity.update;

import net.buffers.OutputBuffer;
import util.Preconditions;
import world.entity.player.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The type Player update block.
 */
public class PlayerUpdateBlock extends UpdateBlock<IFlag<PlayerUpdateFlags.PlayerUpdateMask>> {

    private static final Map<PlayerUpdateFlags.PlayerUpdateMask, BiConsumer<Player, OutputBuffer>> flagMap =
            Collections.unmodifiableMap(new HashMap<PlayerUpdateFlags.PlayerUpdateMask, BiConsumer<Player, OutputBuffer>>() {
                {
                    /*
                        Handles each type of update mask.
                     */
                    put(PlayerUpdateFlags.PlayerUpdateMask.ANIMATION, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.APPEARANCE, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.CHAT, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.DOUBLE_HIT, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.SINGLE_HIT, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.ENTITY_INTERACTION, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.FACE_COORDINATE, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.FORCE_MOVEMENT, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.FORCED_CHAT, (entity, outputBuffer) -> {

                    });

                    put(PlayerUpdateFlags.PlayerUpdateMask.GRAPHICS, (entity, outputBuffer) -> {

                    });
                }
            });

    private final Player player;

    /**
     * Instantiates a new Player update block.
     *
     * @param player the player
     */
    public PlayerUpdateBlock(Player player) {
        Preconditions.notNull(player);
        this.player = player;
    }


    @Override
    public OutputBuffer build(IFlag<PlayerUpdateFlags.PlayerUpdateMask> updateFlags) {
        Preconditions.notNull(updateFlags);

        long mask = updateFlags.getMask();

        OutputBuffer buf = OutputBuffer.create(4096, 1024);

        for (PlayerUpdateFlags.PlayerUpdateMask m : PlayerUpdateFlags.PlayerUpdateMask.values()) {
            if (updateFlags.isSet(m)) {
                PlayerUpdateBlock.flagMap.get(m).accept(player, buf);
            }
        }
        return buf;
    }
}
