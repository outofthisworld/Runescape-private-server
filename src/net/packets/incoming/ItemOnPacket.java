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

import net.network.Client;
import net.buffers.InputBuffer;
import net.packets.exceptions.InvalidOpcodeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ItemOnPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Opcodes.ITEM_ON_FLOOR, Opcodes.ITEM_ON_ITEM, Opcodes.ITEM_ON_NPC, Opcodes.ITEM_ON_OBJECT, Opcodes.ITEM_ON_PLAYER)));


    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        switch (packetOpcode) {
        /**
         Description
         This packet is sent when a player uses an item on another item thats on the floor.

         Packet Structure
         Data Type	Description
         Little Endian Short	The interface ID.
         Unsigned Short Special A	The item being used ID.
         Short	The floor items ID.
         Unsigned Short Special A	The Y coordinate of the item.
         Unsigned Little Endian Short Special A	The items slot ID.
         Short	The X coordinate of the item.*/
        case Opcodes.ITEM_ON_FLOOR:
            break;
        /**
         Description
         This packet is sent when a player uses an item on another item.

         Packet Structure
         Data Type	Description
         Short	The item being used on's slot.
         Short Special A	The item being used's slot.*/
        case Opcodes.ITEM_ON_ITEM:
            break;

        /**
         Sent when a player uses an item on an NPC.
         Unknown packet structure at this time
         */
        case Opcodes.ITEM_ON_NPC:
            break;
        /**
         Description
         This packet is sent when a player uses an item on object.

         Packet Structure
         Data Type	Description
         Short Special A	The frame ID.
         Little Endian Short	The object ID.
         Big Endian Short Special A	The objects Y coordinate.
         Big Endian Short	The items slot ID.
         Big Endian Short Special A	The objects X coordinate.
         Short	The item ID.
         */
        case Opcodes.ITEM_ON_OBJECT:
            break;
        /**
         Description
         This packet is sent when a player uses an item on another player.

         Packet Structure
         Data Type	Description
         Short Special A	The frame ID.
         Short	The other players ID.
         Short	The item ID.
         Little Endian Short	The items slot ID.*/
        case Opcodes.ITEM_ON_PLAYER:
            break;
        default:
            throw new InvalidOpcodeException(packetOpcode, "Invalid opcode " + packetOpcode + " handled by class : " + getClass().getName());
        }
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }
}
