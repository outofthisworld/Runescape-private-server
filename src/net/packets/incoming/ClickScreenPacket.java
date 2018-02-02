package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

/*
    Sent when the player clicks anywhere on the game screen.
*/
public class ClickScreenPacket extends Packet {
    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 241;
    }
}
