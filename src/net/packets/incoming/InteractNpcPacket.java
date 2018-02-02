package net.packets.incoming;

import net.Client;
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

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }
}
