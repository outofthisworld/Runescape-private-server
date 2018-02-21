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

public class WalkingPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Opcodes.PLAYER_WALK, Opcodes.MAP_WALK, Opcodes.WALK_ON, Opcodes.WALKING_ANTI_CHEAT)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        if (!handlesOpcode(packetOpcode)) {
            return;
        }

        int size = in.readBigUnsignedWORD();

        if (packetOpcode == 248) {
            size -= 14;
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
