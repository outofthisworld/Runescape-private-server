package world.entity.update;

import javafx.animation.Animation;
import net.buffers.OutputBuffer;
import util.Preconditions;
import world.entity.player.Player;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The type Player update block.
 */
public class PlayerUpdateBlock extends UpdateBlock<IFlag<PlayerUpdateMask>> {

    private static final Map<PlayerUpdateMask, BiConsumer<Player, OutputBuffer>> flagMap =
            Collections.unmodifiableMap(new HashMap<update.PlayerUpdateMask, BiConsumer<Player, OutputBuffer>>() {
                {
                    /**Handles each type of update mask.*/

                    /**
                     The animation portion of the update block.
                     */
                    put(update.PlayerUpdateMask.ANIMATION, (entity, outputBuffer) -> {
                        Animation animation = entity.getAnimation();
                        outputBuffer.writeShort(animation.getId(), ByteOrder.LITTLE).write(animation.getDelay(), ByteModification.NEGATION);
                    });


                    /**
                     The appearance portion of the update block.
                     */
                    put(update.PlayerUpdateMask.APPEARANCE, (entity, outputBuffer) -> {
                        PacketWriter properties = new PacketWriter(ByteBuffer.allocate(128));
                        properties.write(entity.getAppearance().getGender().getIndicator());
                        properties.write(0);
                        properties.write(0);
                        properties.write(0);
                        properties.write(0);
                        properties.write(0);
                        properties.writeShort(0x100 + entity.getAppearance().getAppearanceIndices()[0]);
                        properties.write(0);
                        properties.writeShort(0x100 + entity.getAppearance().getAppearanceIndices()[1]);
                        properties.writeShort(0x100 + entity.getAppearance().getAppearanceIndices()[2]);
                        properties.writeShort(0x100 + entity.getAppearance().getAppearanceIndices()[3]);
                        properties.writeShort(0x100 + entity.getAppearance().getAppearanceIndices()[4]);
                        properties.writeShort(0x100 + entity.getAppearance().getAppearanceIndices()[5]);

                        if (entity.getAppearance().getGender().getIndicator() == 0) {
                            properties.writeShort(0x100 + entity.getAppearance().getAppearanceIndices()[6]);
                        } else {
                            properties.write(0);
                        }
                        properties.write(entity.getAppearance().getColorIndices()[0]);
                        properties.write(entity.getAppearance().getColorIndices()[1]);
                        properties.write(entity.getAppearance().getColorIndices()[2]);
                        properties.write(entity.getAppearance().getColorIndices()[3]);
                        properties.write(entity.getAppearance().getColorIndices()[4]);
                        properties.writeShort(0x328);
                        properties.writeShort(0x337);
                        properties.writeShort(0x333);
                        properties.writeShort(0x334);
                        properties.writeShort(0x335);
                        properties.writeShort(0x336);
                        properties.writeShort(0x338);
                        properties.writeLong(LongUtils.convertStringToLong(entity.getDetails().getUsername()));
                        properties.write(3);
                        properties.writeShort(0);
                        buffer.write(properties.getBuffer().position(), ByteModification.NEGATION);
                        buffer.writeBytes(properties.getBuffer());
                    });

                    /**
                     The chat portion of the update block.
                     */
                    put(update.PlayerUpdateMask.CHAT, (entity, outputBuffer) -> {
                        ChatMessage msg = entity.getChatMessage();
                        byte[] bytes = msg.getText();

                        writer.writeShort(((msg.getColor() & 0xFF) << 8) + (msg.getEffect() & 0xFF), ByteOrder.LITTLE)
                                .write(entity.getRights().getProtocolValue()).write(bytes.length, ByteModification.NEGATION)
                                .writeBytesReverse(bytes);
                    });

                    /**
                     The double hit portion of the update block.
                     */
                    put(update.PlayerUpdateMask.DOUBLE_HIT, (entity, outputBuffer) -> {
                        builder.put(entity.getSecondaryHit().getDamage())
                                .put(entity.getSecondaryHit().getType().getId(), ByteValue.SUBTRACTION)
                                .put(entity.getSkill().getLevel(Skill.HITPOINTS))
                                .put(entity.getSkill().getMaxLevel(Skill.HITPOINTS), ByteValue.NEGATION);
                    });

                    /**
                     The single hit portion of the update block.
                     */
                    put(update.PlayerUpdateMask.SINGLE_HIT, (entity, outputBuffer) -> {
                        builder.put(entity.getPrimaryHit().getDamage())
                                .put(entity.getPrimaryHit().getType().getId(), ByteValue.ADDITION)
                                .put(entity.getSkill().getLevel(Skill.HITPOINTS), ByteValue.NEGATION)
                                .put(entity.getSkill().getMaxLevel(Skill.HITPOINTS));
                    });

                    /**
                     The entity interaction portion of the update block.
                     */
                    put(update.PlayerUpdateMask.ENTITY_INTERACTION, (entity, outputBuffer) -> {
                        Entity entity = target.getInteractingEntity();

                        if (entity != null) {
                            int index = entity.getSlot();

                            if (entity instanceof Player) {
                                index += +32768;
                            }

                            builder.writeShort(index, ByteOrder.LITTLE);
                        } else {
                            builder.writeShort(-1, ByteOrder.LITTLE);
                        }
                    });

                    /**
                     The face coordinate portion of the update block.
                     */
                    put(update.PlayerUpdateMask.FACE_COORDINATE, (entity, outputBuffer) -> {
                        builder.writeShort((Integer) entity.getAttributes().get(Attributes.FACING_COORDINATE_X),
                                ByteModification.ADDITION, ByteOrder.LITTLE)
                                .writeShort((Integer) entity.getAttributes()
                                        .get(Attributes.FACING_COORDINATE_Y), ByteOrder.LITTLE);
                    });

                    /**
                     The force movement portion of the update block.
                     */
                    put(update.PlayerUpdateMask.FORCE_MOVEMENT, (entity, outputBuffer) -> {
                        ForceMovement movement = entity.getForceMovement();

                        writer.write(movement.getStartLocation().getX(), ByteModification.ADDITION)
                                .write(movement.getStartLocation().getY(), ByteModification.NEGATION)
                                .write(movement.getEndLocation().getX(), ByteModification.SUBTRACTION).write(movement.getEndLocation().getY())
                                .writeShort(movement.getDurationX()).write(movement.getDurationY(), ByteModification.ADDITION)
                                .write(movement.getDirection().getId());
                    });

                    /**
                     The forced chat portion of the update block.
                     */
                    put(update.PlayerUpdateMask.FORCED_CHAT, (entity, outputBuffer) -> {
                        builder.writeString(entity.getForcedChat());
                    });

                    /**
                     The graphics portion of the update block.
                     */
                    put(update.PlayerUpdateMask.GRAPHICS, (entity, outputBuffer) -> {
                        Graphic graphic = entity.getGraphic();
                        outputBuffer.writeShort(graphic.getId(), ByteOrder.LITTLE);
                        outputBuffer.writeInt(graphic.getDelay() | graphic.getHeight());
                    });
                }
            });

    private static final int UPDATE_BLOCK_SIZE = 4096;
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
            Clear the current update block.
        */
        updateBlock.order(OutputBuffer.Order.LITTLE_ENDIAN);

        /*
            Write the mask as little endian.
        */
        updateBlock.writeBytes(updateFlags.getMask(), 2, OutputBuffer.ByteTransformationType.NONE);


        for (PlayerUpdateMask m : PlayerUpdateMask.values()) {
            if (updateFlags.isSet(m)) {
                PlayerUpdateBlock.flagMap.get(m).accept(player, updateBlock);
            }
        }
    }

    @Override
    public OutputBuffer getBlock() {
        return updateBlock;
    }
}
