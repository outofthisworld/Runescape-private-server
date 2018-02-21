package world.entity.update.player;

import net.buffers.ByteTransformationType;
import net.buffers.IBufferReserve;
import net.buffers.Order;
import net.buffers.OutputBuffer;
import util.Preconditions;
import util.RsUtils;
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
                    put(PlayerUpdateMask.ANIMATION, (entity, outputBuffer) -> {
                        //Animation animation = entity.getAnimation();
                        //outputBuffer.writeShort(animation.getId(), ByteOrder.LITTLE).write(animation.getDelay(), ByteModification.NEGATION);
                    });


                    /**
                     The appearance portion of the update block.
                     */
                    put(PlayerUpdateMask.APPEARANCE, (entity, outputBuffer) -> {
                        IBufferReserve<OutputBuffer> reserve = outputBuffer.createByteReserve(1);
                        outputBuffer.writeByte(entity.getAppearance().getGender());
                        //Dont know what these do...
                        outputBuffer.writeByte(0);
                        outputBuffer.writeByte(0);
                        outputBuffer.writeByte(0);
                        outputBuffer.writeByte(0);
                        outputBuffer.writeByte(0);

                        outputBuffer.writeBigWORD(0x100 + entity.getAppearance().getAppearenceIndice(0));
                        outputBuffer.writeByte(0);
                        outputBuffer.writeBigWORD(0x100 + entity.getAppearance().getAppearenceIndice(1));
                        outputBuffer.writeBigWORD(0x100 + entity.getAppearance().getAppearenceIndice(2));
                        outputBuffer.writeBigWORD(0x100 + entity.getAppearance().getAppearenceIndice(3));
                        outputBuffer.writeBigWORD(0x100 + entity.getAppearance().getAppearenceIndice(4));
                        outputBuffer.writeBigWORD(0x100 + entity.getAppearance().getAppearenceIndice(5));

                        if (entity.getAppearance().getGender() == 0) {
                            outputBuffer.writeBigWORD(0x100 + entity.getAppearance().getAppearenceIndice(6));
                        } else {
                            outputBuffer.writeByte(0);
                        }

                        outputBuffer.writeByte(entity.getAppearance().getColorIndice(0));
                        outputBuffer.writeByte(entity.getAppearance().getColorIndice(1));
                        outputBuffer.writeByte(entity.getAppearance().getColorIndice(2));
                        outputBuffer.writeByte(entity.getAppearance().getColorIndice(3));
                        outputBuffer.writeByte(entity.getAppearance().getColorIndice(4));

                        //Dont know what these do
                        outputBuffer.writeBigWORD(0x328);
                        outputBuffer.writeBigWORD(0x337);
                        outputBuffer.writeBigWORD(0x333);
                        outputBuffer.writeBigWORD(0x334);
                        outputBuffer.writeBigWORD(0x335);
                        outputBuffer.writeBigWORD(0x336);
                        outputBuffer.writeBigWORD(0x338);

                        outputBuffer.writeBigQWORD(RsUtils.convertStringToLong(entity.getUsername()));
                        outputBuffer.writeByte(3);
                        outputBuffer.writeBigWORD(0);
                        reserve.writeValue(reserve.bytesSinceReserve(), ByteTransformationType.C);
                    });

                    /**
                     The chat portion of the update block.
                     */
                    put(PlayerUpdateMask.CHAT, (entity, outputBuffer) -> {
                        /*ChatMessage msg = entity.getChatMessage();
                        byte[] bytes = msg.getText();

                        writer.writeShort(((msg.getColor() & 0xFF) << 8) + (msg.getEffect() & 0xFF), ByteOrder.LITTLE)
                                .write(entity.getRights().getProtocolValue()).write(bytes.length, ByteModification.NEGATION)
                                .writeBytesReverse(bytes);*/
                    });

                    /**
                     The double hit portion of the update block.
                     */
                    put(PlayerUpdateMask.DOUBLE_HIT, (entity, outputBuffer) -> {
                        /*builder.put(entity.getSecondaryHit().getDamage())
                                .put(entity.getSecondaryHit().getType().getId(), ByteValue.SUBTRACTION)
                                .put(entity.getSkill().getLevel(Skill.HITPOINTS))
                                .put(entity.getSkill().getMaxLevel(Skill.HITPOINTS), ByteValue.NEGATION);*/
                    });

                    /**
                     The single hit portion of the update block.
                     */
                    put(PlayerUpdateMask.SINGLE_HIT, (entity, outputBuffer) -> {
                        /*builder.put(entity.getPrimaryHit().getDamage())
                                .put(entity.getPrimaryHit().getType().getId(), ByteValue.ADDITION)
                                .put(entity.getSkill().getLevel(Skill.HITPOINTS), ByteValue.NEGATION)
                                .put(entity.getSkill().getMaxLevel(Skill.HITPOINTS));*/
                    });

                    /**
                     The entity interaction portion of the update block.
                     */
                    put(PlayerUpdateMask.ENTITY_INTERACTION, (entity, outputBuffer) -> {
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
                    });

                    /**
                     The face coordinate portion of the update block.
                     */
                    put(PlayerUpdateMask.FACE_COORDINATE, (entity, outputBuffer) -> {
                        /*builder.writeShort((Integer) entity.getAttributes().get(Attributes.FACING_COORDINATE_X),
                                ByteModification.ADDITION, ByteOrder.LITTLE)
                                .writeShort((Integer) entity.getAttributes()
                                        .get(Attributes.FACING_COORDINATE_Y), ByteOrder.LITTLE);*/
                    });

                    /**
                     The force movement portion of the update block.
                     */
                    put(PlayerUpdateMask.FORCE_MOVEMENT, (entity, outputBuffer) -> {
                        /*ForceMovement movement = entity.getForceMovement();

                        writer.write(movement.getStartLocation().getX(), ByteModification.ADDITION)
                                .write(movement.getStartLocation().getY(), ByteModification.NEGATION)
                                .write(movement.getEndLocation().getX(), ByteModification.SUBTRACTION).write(movement.getEndLocation().getY())
                                .writeShort(movement.getDurationX()).write(movement.getDurationY(), ByteModification.ADDITION)
                                .write(movement.getDirection().getId());*/
                    });

                    /**
                     The forced chat portion of the update block.
                     */
                    put(PlayerUpdateMask.FORCED_CHAT, (entity, outputBuffer) -> {
                        /*builder.writeString(entity.getForcedChat());*/
                    });

                    /**
                     The graphics portion of the update block.
                     */
                    put(PlayerUpdateMask.GRAPHICS, (entity, outputBuffer) -> {
                        /*Graphic graphic = entity.getGraphic();
                        outputBuffer.writeShort(graphic.getId(), ByteOrder.LITTLE);
                        outputBuffer.writeInt(graphic.getDelay() | graphic.getHeight());*/
                    });
                }
            });

    private static final int UPDATE_BLOCK_SIZE = 2048;
    private static final int UPDATE_BLOCK_INCREASE_SIZE = 1024;
    private final Player player;
    private final OutputBuffer updateBlock = OutputBuffer.create(PlayerUpdateBlock.UPDATE_BLOCK_SIZE, PlayerUpdateBlock.UPDATE_BLOCK_INCREASE_SIZE);

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

    @Override
    public OutputBuffer getBlock() {
        return updateBlock;
    }
}
