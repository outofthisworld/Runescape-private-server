package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

/*
    Sent when the player clicks anywhere on the game screen.
*/
public class ClickScreenPacket extends Packet {
    private static final Logger logger = Logger.getLogger(ClickScreenPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }


    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 241;
    }
}
