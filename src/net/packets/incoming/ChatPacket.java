/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package net.packets.incoming;

import net.buffers.InputBuffer;
import net.impl.session.Client;

import java.util.Set;
import java.util.logging.Logger;

public class ChatPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == Opcodes.CHAT_PACKET;
    }

    @Override
    public Set<Integer> getOpcodes() {
        return null;
    }
}
