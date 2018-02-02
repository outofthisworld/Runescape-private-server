package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

public class CommandPacket extends Packet {
    private static final Logger logger = Logger.getLogger(CommandPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }


    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 103;
    }
}
