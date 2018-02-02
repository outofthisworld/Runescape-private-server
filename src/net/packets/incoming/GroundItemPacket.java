package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static net.packets.incoming.IncomingPacket.Opcodes.GROUND_ITEM_ACTION_1;
import static net.packets.incoming.IncomingPacket.Opcodes.PICKUP_GROUND_ITEM;


public class GroundItemPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(PICKUP_GROUND_ITEM, GROUND_ITEM_ACTION_1)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }
}
