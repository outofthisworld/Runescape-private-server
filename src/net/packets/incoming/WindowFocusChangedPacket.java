

package net.packets.incoming;

import net.buffers.InputBuffer;
import net.impl.session.Client;

import java.util.Set;
import java.util.logging.Logger;

public class WindowFocusChangedPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());


    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        // This packet is sent when the game client window goes in and out of focus.
        // The payload consists of one byte that is either 1 or 0; 1 if the client is in focus and 0 if not.
        // Byte	Whether or not the client is in focus.
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == Opcodes.WINDOW_FOCUS_CHANGE;
    }

    @Override
    public Set<Integer> getOpcodes() {
        return null;
    }
}
