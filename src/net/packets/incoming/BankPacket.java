package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;
import net.packets.exceptions.InvalidOpcodeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class BankPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Opcodes.BANK_5, Opcodes.BANK_10, Opcodes.BANK_ALL_ITEMS, Opcodes.BANK_X_ITEMS_1, Opcodes.BANK_X_ITEMS_2, Opcodes.VALIDATE_BANKING_ANTI_CHEAT)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

        switch (packetOpcode) {
        //This packet is sent when a player attempts to bank 5 of a certain item.
        //Note: This packet is also used for buying/selling 1 of an item from a shop.
        //Little endian Short Special A - Frame ID
        //Little endian Short Special A - Item ID
        //Little endian Short - The slot ID
        case Opcodes.BANK_5:
            break;
        //This packet is sent when the player attempts to bank 10 of a certain item.
        //Note: This packet is also used for selling/buying 5 of an item from a shop.
        //Little Endian Short	The frame ID.
        //Short Special A	The item ID.
        //Short Special A	The slot ID.
        case Opcodes.BANK_10:
            break;
        //This packet is sent when a player banks all of a certain item they have in their inventory.
        //Note: This packet is also used for selling/buying 10 items at a shop.
        //Unsigned Short Special A	The items slot ID.
        //Unsigned Short	The interface ID.
        //Unsigned Short Special A	The item ID.
        case Opcodes.BANK_ALL_ITEMS:
            break;
        //This packet is sent when a player requests to bank an X amount of items.
        //Little Endian Short	The items slot.
        //Unsigned Short Special A	The interface ID.
        //Little Endian Short	The item ID.
        case Opcodes.BANK_X_ITEMS_1:
            break;
        //This packet is sent when a player enters an X amount of items they want to bank.
        //Integer	The amount of the item you want to bank
        case Opcodes.BANK_X_ITEMS_2:
            break;
        case Opcodes.VALIDATE_BANKING_ANTI_CHEAT:
            break;
        default:
            throw new InvalidOpcodeException(packetOpcode, "Invalid opcode " + packetOpcode + " handled by class: " + getClass().getName());
        }

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }
}
