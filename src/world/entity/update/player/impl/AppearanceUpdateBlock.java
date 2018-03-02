package world.entity.update.player.impl;

import net.buffers.ByteTransformationType;
import net.buffers.IBufferReserve;
import net.buffers.OutputBuffer;
import util.RsUtils;
import world.entity.player.Appearance;
import world.entity.player.AppearanceSlot;
import world.entity.player.EquipmentSlot;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class AppearanceUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player entity, OutputBuffer outputBuffer) {
        /*
            Create a one byte reserve to hold size of update block.
        */
        IBufferReserve<OutputBuffer> reserve = outputBuffer.createByteReserve(1);
        /*
            Write players gender.
        */
        outputBuffer.writeByte(entity.getAppearance().getGender().getId());
        /*
            Write players head icon.
        */
        outputBuffer.writeByte(entity.getAppearance().getHeadIcon().getId());
        /*
            Write players skull icon.
        */
        outputBuffer.writeByte(entity.getAppearance().getSkullIcon().getId());
        /*
            Write players hint icon.
        */
        outputBuffer.writeByte(-1);

        /* Player equipment/show model updates */
        if (!entity.getEquipment().getContainer().isEmpty(EquipmentSlot.HEAD.getSlotId())) {
            outputBuffer.writeBigWord(0x200 + entity.getEquipment().get(EquipmentSlot.HEAD.getSlotId()).getItemDefinition().getId()); //head
        } else {
            System.out.println("head not empty");
            outputBuffer.writeByte(0);
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
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.CHEST)); //chest - torso
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

        if (!entity.getEquipment().getContainer().isEmpty(EquipmentSlot.HEAD.getSlotId()) &&
                entity.getEquipment().getContainer().get(EquipmentSlot.HEAD.getSlotId()).getItemDefinition().isFullHelm()) {
            outputBuffer.writeByte(0);
        } else {
            outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.HEAD));
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

        if (entity.getAppearance().getGender() == Appearance.Gender.MALE) {
            if (
                    (!entity.getEquipment().getContainer().isEmpty(EquipmentSlot.HEAD.getSlotId())
                            && entity.getEquipment().get(EquipmentSlot.HEAD.getSlotId()).getItemDefinition().isFullHelm())
                            || entity.getEquipment().getContainer().isEmpty(EquipmentSlot.HEAD.getSlotId())

                    ) {

                    outputBuffer.writeBigWord(0x100 + entity.getAppearance().getAppearance(AppearanceSlot.BEARD)
                );
            } else {
                outputBuffer.writeByte(0);
            }
        } else {
            outputBuffer.writeByte(0);
        }

        /* Player hair color */
        outputBuffer.writeByte(entity.getAppearance().getHairColor());
        /* Player torso color */
        outputBuffer.writeByte(entity.getAppearance().getTorsoColor());
        /* Player leg color */
        outputBuffer.writeByte(entity.getAppearance().getLegColor());
        /* Player feet color */
        outputBuffer.writeByte(entity.getAppearance().getFeetColor());
        /* Player skin color */
        outputBuffer.writeByte(entity.getAppearance().getSkinColor());

        /* Weapon animation standing (idle animation) */
        outputBuffer.writeBigWord(0x328);
        /* Stand turn anim index (standTurnAnimIndex) */
        outputBuffer.writeBigWord(0x337);
        /* Weapon animation walking (walkAnimIndex) */
        outputBuffer.writeBigWord(0x333);
        /*turn180AnimIndex */
        outputBuffer.writeBigWord(0x334);
        /*turn90CWAnimIndex*/
        outputBuffer.writeBigWord(0x335);
        /*turn90CCWAnimIndex*/
        outputBuffer.writeBigWord(0x336);
        /*Weapon animation running (runAnimIndex) */
        outputBuffer.writeBigWord(0x338);

        /* Player username encoded as long */
        outputBuffer.writeBigQWORD(RsUtils.convertStringToLong(entity.getUsername()));
        /* Player combat level */
        outputBuffer.writeByte(3);
         /* Player combat level */
        outputBuffer.writeBigWord(0);
        /* Write length of appearance update block */
        reserve.writeValue(reserve.bytesSinceReserve(), ByteTransformationType.C);
    }
}
