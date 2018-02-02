package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

public class CommandPacket extends Packet {
    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 103;
    }
}
