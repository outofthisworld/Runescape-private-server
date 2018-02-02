package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

public class ClickButtonPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(ClickButtonPacket.class.getName());

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
