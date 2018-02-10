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

import net.impl.Client;
import net.buffers.InputBuffer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class InteractNpcPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Opcodes.ATTACK_NPC, Opcodes.NPC_ACTION_1, Opcodes.NPC_ACTION_2, Opcodes.NPC_ACTION_3, Opcodes.NPC_ACTION_4, Opcodes.NPC_OPTION_2_ANTI_CHEAT, Opcodes.NPC_OPTION_3_ANTI_CHEAT, Opcodes.NPC_OPTION_4_ANTI_CHEAT)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

        switch (packetOpcode) {
        /**
         Description
         This packet is sent when a player attacks an NPC.

         Packet Structure
         Data Type	Description
         Unsigned Short Special A	The NPC ID.
         */
        case Opcodes.ATTACK_NPC:
            break;
        /**
         Description
         This packet is sent when a player clicks the first option of an NPC.

         Packet Structure
         Data Type	Description
         Little Endian Short
         */
        case Opcodes.NPC_ACTION_1:
            break;
        /**
         Description
         This packet is sent when a player clicks the second action of an NPC.

         Packet Structure
         Data Type	Description
         Little Endian Short Special A */
        case Opcodes.NPC_ACTION_2:
            break;
        /**
         Description
         This packet is sent when a player clicks the third option of an NPC.

         Packet Structure
         Data Type	Description
         Unsigned Little Endian Short Special A	The NPC index.*/
        case Opcodes.NPC_ACTION_3:
            break;
        case Opcodes.NPC_ACTION_4:
            break;
        case Opcodes.NPC_OPTION_2_ANTI_CHEAT:
            break;
        case Opcodes.NPC_OPTION_3_ANTI_CHEAT:
            break;
        case Opcodes.NPC_OPTION_4_ANTI_CHEAT:
            break;
        }

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }
}
