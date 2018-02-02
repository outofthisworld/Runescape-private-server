package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

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

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }
}
