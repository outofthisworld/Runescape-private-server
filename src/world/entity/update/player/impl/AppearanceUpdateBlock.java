package world.entity.update.player.impl;

import net.buffers.ByteTransformationType;
import net.buffers.IBufferReserve;
import net.buffers.OutputBuffer;
import util.RsUtils;
import world.entity.player.AppearanceSlot;
import world.entity.player.EquipmentSlot;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class AppearanceUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player entity, OutputBuffer outputBuffer) {
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
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.HEAD)); //head
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.HEAD.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.CAPE.getSlotId())) {
            outputBuffer.writeByte(0);
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.CAPE.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.AMULET.getSlotId())) {
            outputBuffer.writeByte(0);
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.AMULET.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.WEAPON.getSlotId())) {
            outputBuffer.writeByte(0);
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.WEAPON.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.CHEST.getSlotId())) {
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.TORSO)); //chest - torso
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.CHEST.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.CHEST.getSlotId())) {
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.ARMS));
        } else {
            outputBuffer.writeByte(0);
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.SHIELD.getSlotId())) {
            outputBuffer.writeByte(0);
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.SHIELD.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.LEGS.getSlotId())) {
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.LEGS));
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.LEGS.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.HANDS.getSlotId())) {
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.HANDS));
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.HANDS.getSlotId()).getItemDefinition().getId());
        }

        if (entity.getEquipment().getContainer().isEmpty(EquipmentSlot.FEET.getSlotId())) {
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.FEET));
        } else {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().getContainer().get(EquipmentSlot.FEET.getSlotId()).getItemDefinition().getId());
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
    }
}
