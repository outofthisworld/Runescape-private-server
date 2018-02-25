package world.entity.update.player;

import net.buffers.ByteTransformationType;
import net.buffers.IBufferReserve;
import net.buffers.Order;
import net.buffers.OutputBuffer;
import util.Preconditions;
import util.RsUtils;
import world.entity.player.AppearanceSlot;
import world.entity.player.EquipmentSlot;
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
                        outputBuffer.writeByte(0);
                        /*
                            HEAD(0),
                            CAPE(1),
                            AMULET(2),
                            WEAPON(3),
                            CHEST(4),
                            SHIELD(5),
                            LEGS(7),
                            HANDS(9),
                            FEET(10),
                            RING(12),
                            ARROWS(13);
                         */
                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.HEAD.getSlotId())) {
                            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearence(AppearanceSlot.HEAD)); //head
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.HEAD.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.CAPE.getSlotId())) {
                            outputBuffer.writeByte(0);
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.CAPE.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.AMULET.getSlotId())) {
                            outputBuffer.writeByte(0);
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.AMULET.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.WEAPON.getSlotId())) {
                            outputBuffer.writeByte(0);
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.WEAPON.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.CHEST.getSlotId())) {
                            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearence(AppearanceSlot.TORSO)); //chest - torso
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.CHEST.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.CHEST.getSlotId())) {
                            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearence(AppearanceSlot.ARMS));
                        } else {
                            outputBuffer.writeByte(0);
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.SHIELD.getSlotId())) {
                            outputBuffer.writeByte(0);
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.SHIELD.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.LEGS.getSlotId())) {
                            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearence(AppearanceSlot.LEGS));
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.LEGS.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.HANDS.getSlotId())) {
                            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearence(AppearanceSlot.HANDS));
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.HANDS.getSlotId()).getItemDefinition().get().getId());
                        }

                        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.FEET.getSlotId())) {
                            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearence(AppearanceSlot.FEET));
                        } else {
                            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.FEET.getSlotId()).getItemDefinition().get().getId());
                        }


                        outputBuffer.writeByte(0);


                        outputBuffer.writeByte(entity.getAppearance().getColor(AppearanceSlot.HEAD));
                        outputBuffer.writeByte(entity.getAppearance().getColor(AppearanceSlot.TORSO));
                        outputBuffer.writeByte(entity.getAppearance().getColor(AppearanceSlot.ARMS));
                        outputBuffer.writeByte(entity.getAppearance().getColor(AppearanceSlot.LEGS));
                        outputBuffer.writeByte(entity.getAppearance().getColor(AppearanceSlot.HANDS));
                        outputBuffer.writeByte(entity.getAppearance().getColor(AppearanceSlot.FEET));

                        //Dont know what these do
                        outputBuffer.writeBigWord(0x328);
                        outputBuffer.writeBigWord(0x337);
                        outputBuffer.writeBigWord(0x333);
                        outputBuffer.writeBigWord(0x334);
                        outputBuffer.writeBigWord(0x335);
                        outputBuffer.writeBigWord(0x336);
                        outputBuffer.writeBigWord(0x338);

                        outputBuffer.writeBigQWORD(RsUtils.convertStringToLong(entity.getUsername()));
                        outputBuffer.writeByte(3);
                        outputBuffer.writeBigWord(0);
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
                                .write(movement.getWalkDirection().getId());*/
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
