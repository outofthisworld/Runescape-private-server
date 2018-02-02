package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

public class ChatOptionsPacket extends Packet {

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == IncomingPackets.CHAT_OPTIONS;
    }
}
