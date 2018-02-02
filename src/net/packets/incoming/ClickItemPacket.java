package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

public class ClickItemPacket extends Packet {
    private static final Logger logger = Logger.getLogger(ClickItemPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        /*in.readShort(true, ValueType.A, ByteOrder.LITTLE);
        int slot = in.readShort(false, ValueType.A);
        int id = in.readShort(false, ByteOrder.LITTLE);
        SkillEvent.fireSkillEvents(player);

        if (player.getInventory().getContainer().isSlotFree(slot)) {
            return;
        }

        if (id != player.getInventory().getContainer().getIdBySlot(slot)) {
            return;
        }

        ConsumeFood.consume(player, Food.forId(id), slot);
        Prayer.getSingleton().buryItem(player, PrayerItem.getPrayerItem(id), slot);

        switch (id) {
            /** ... */
    }


    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 122;
    }
}
