package net.packets.incoming;

import net.buffers.InputBuffer;
import net.impl.session.Client;
import world.WorldManager;

import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ForwardDialoguePacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private static final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Opcodes.FORWARD_DIALOGUE)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
       c.getPlayer().getDialogueHandler().forwardDialogue();
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == Opcodes.FORWARD_DIALOGUE;
    }

    @Override
    public Set<Integer> getOpcodes() {
        return opcodes;
    }
}
