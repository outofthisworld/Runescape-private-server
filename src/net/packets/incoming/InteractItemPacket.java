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

import net.Client;
import net.buffers.InputBuffer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static net.packets.incoming.IncomingPacket.Opcodes.*;

public class InteractItemPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());

    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ITEM_ACTION_1, ITEM_ACTION_3, EQUIP_ITEM, ALTERNATE_ITEM_OPTION, UNEQUIP_ITEM, MOVE_ITEM, DROP_ITEM_GROUND, LIGHT_ITEM)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }
}
