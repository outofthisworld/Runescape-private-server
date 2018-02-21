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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static net.packets.incoming.IncomingPacket.Opcodes.IDLE;
import static net.packets.incoming.IncomingPacket.Opcodes.IDLE_LOGOUT;


public class IdlePacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(IDLE, IDLE_LOGOUT)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        switch (packetOpcode) {
            //Sent when the player is idle for the current cycle, and acts as a "ping" packet.
            case Opcodes.IDLE:
                break;
            case Opcodes.IDLE_LOGOUT:
                break;
        }
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }

    @Override
    public Set<Integer> getOpcodes() {
        return opcodes;
    }
}
