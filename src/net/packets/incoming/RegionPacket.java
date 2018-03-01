

package net.packets.incoming;

import net.buffers.InputBuffer;
import net.impl.session.Client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static net.packets.incoming.IncomingPacket.Opcodes.REGION_CHANGE;
import static net.packets.incoming.IncomingPacket.Opcodes.REGION_LOADED;

public class RegionPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());

    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(REGION_CHANGE, REGION_LOADED)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

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
