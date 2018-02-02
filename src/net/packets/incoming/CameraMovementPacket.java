package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

public class CameraMovementPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == Opcodes.CAMERA_MOVEMENT;
    }
}
