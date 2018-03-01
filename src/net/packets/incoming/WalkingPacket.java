

package net.packets.incoming;

import net.buffers.ByteTransformationType;
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

        int size = in.remaining();

        if (packetOpcode == 248) {
            size -= 14; // strip off anti-cheat data
        }

        int steps = (size - 5) / 2;


        if (steps < 0) {
            return;
        }

        int[][] path = new int[steps][2];

        int targetX = in.readLittleUnsignedWordTypeA();

        for (int i = 0; i < steps; i++) {
            path[i][0] = in.readSignedByte();
            path[i][1] = in.readSignedByte();
        }

        int targetY = in.readLittleUnsignedWORD();
        int run = in.readUnsignedByte(ByteTransformationType.C);
        boolean shouldRun = run == 1;
        System.out.println("should run: " + shouldRun);

        c.getPlayer().getMovement().setRunning(true);
        c.getPlayer().getMovement().beginMovement();
        c.getPlayer().getMovement().stepTo(targetX, targetY);
        for (int i = 0; i < steps; i++) {
            path[i][0] = path[i][0] + targetX;
            path[i][1] = path[i][1] + targetY;
            c.getPlayer().getMovement().stepTo(path[i][0], path[i][1]);
        }
        c.getPlayer().getMovement().finishMovement();
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
