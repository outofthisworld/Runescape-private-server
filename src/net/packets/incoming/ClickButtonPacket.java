package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

public class ClickButtonPacket extends Packet {
    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        int buttonId = in.readBigUnsignedWORD();

        switch (buttonId) {

        }
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 185;
    }
}
