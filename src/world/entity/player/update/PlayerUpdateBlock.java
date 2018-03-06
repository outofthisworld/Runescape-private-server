package world.entity.player.update;

import net.buffers.ByteTransformationType;
import net.buffers.Order;
import net.buffers.OutputBuffer;
import util.Preconditions;
import world.entity.player.Player;
import world.entity.update.IFlag;
import world.entity.update.UpdateBlock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The type Player update block.
 */
public class PlayerUpdateBlock extends UpdateBlock<IFlag<PlayerUpdateMask>> {

    private static final Map<PlayerUpdateMask, BiConsumer<Player, OutputBuffer>> flagMap =
            Collections.unmodifiableMap(new HashMap<PlayerUpdateMask, BiConsumer<Player, OutputBuffer>>() {
                {
                    /**Handles each type of update mask.*/

                    /**
                     The animation portion of the update block.
                     */
                    put(PlayerUpdateMask.ANIMATION, new AnimationUpdateBlock());

                    /**
                     The appearance portion of the update block.
                     */
                    put(PlayerUpdateMask.APPEARANCE, new AppearanceUpdateBlock());

                    /**
                     The chat portion of the update block.
                     */
                    put(PlayerUpdateMask.CHAT, new ChatUpdateBlock());

                    /**
                     The double hit portion of the update block.
                     */
                    put(PlayerUpdateMask.DOUBLE_HIT, new DoubleHitUpdateBlock());

                    /**
                     The single hit portion of the update block.
                     */
                    put(PlayerUpdateMask.SINGLE_HIT, new SingleHitUpdateBlock());

                    /**
                     The entity interaction portion of the update block.
                     */
                    put(PlayerUpdateMask.ENTITY_INTERACTION, new EntityInteractionUpdateBlock());

                    /**
                     The face coordinate portion of the update block.
                     */
                    put(PlayerUpdateMask.FACE_COORDINATE, new FaceCoordUpdateBlock());

                    /**
                     The force movement portion of the update block.
                     */
                    put(PlayerUpdateMask.FORCE_MOVEMENT, new ForceMovementUpdateBlock());

                    /**
                     The forced chat portion of the update block.
                     */
                    put(PlayerUpdateMask.FORCED_CHAT, new ForcedChatUpdateBlock());

                    /**
                     The graphics portion of the update block.
                     */
                    put(PlayerUpdateMask.GRAPHICS, new GraphicsUpdateBlock());
                }
            });

    private static final int UPDATE_BLOCK_SIZE = 2048;
    private static final int UPDATE_BLOCK_INCREASE_SIZE = 1024;
    private final Player player;
    private final OutputBuffer updateBlock = OutputBuffer.create(PlayerUpdateBlock.UPDATE_BLOCK_SIZE, PlayerUpdateBlock.UPDATE_BLOCK_INCREASE_SIZE);
    private long mask;

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
    public PlayerUpdateBlock build(IFlag<PlayerUpdateMask> updateFlags) {
        Preconditions.notNull(updateFlags);

        this.mask = updateFlags.getMask();

        /*
            Clear the current update block.
        */
        updateBlock.clear();

        /*
            Write the mask as little endian.
        */
        updateBlock.order(Order.LITTLE_ENDIAN);

        if (updateFlags.getMask() >= 0x100L) {
            updateBlock.writeBytes(updateFlags.getMask(), 2, ByteTransformationType.NONE);
        } else {
            updateBlock.writeByte((int) (updateFlags.getMask()));
        }

        updateBlock.order(Order.BIG_ENDIAN);


        for (PlayerUpdateMask m : PlayerUpdateMask.values()) {
            if (updateFlags.isSet(m)) {
                PlayerUpdateBlock.flagMap.get(m).accept(player, updateBlock);
            }
        }
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public long getMask() {
        return mask;
    }

    @Override
    public OutputBuffer getBlock() {
        return updateBlock;
    }
}
