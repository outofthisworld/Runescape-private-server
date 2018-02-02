package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

public class DropItemPacket extends Packet {
    private static final Logger logger = Logger.getLogger(DropItemPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        /*
        int item = in.readShort(false, ValueType.A);

        in.readByte(false);
        in.readByte(false);
        int slot = in.readShort(false, ValueType.A);

        SkillEvent.fireSkillEvents(player);

        int amount = ItemDefinition.getDefinitions()[item].isStackable() ? amount = player.getInventory().getContainer().getCount(item) : 1;

        if (player.getInventory().getContainer().contains(item)) {
            player.getInventory().deleteItemSlot(new Item(item, amount), slot);
            Position itemLocation = new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
            GroundItem.getRegisterable().register(new GroundItem(new Item(item, amount), itemLocation, player));
        }
           */
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 87;
    }
}
