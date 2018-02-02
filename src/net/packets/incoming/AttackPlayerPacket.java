package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;
import world.WorldManager;
import world.entity.Player;

import java.util.logging.Logger;

public class AttackPlayerPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(AttackPlayerPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        int playerIndex = in.readLittleUnsignedWORD();

        Player attackingPlayer = c.getPlayer();
        Player attackedPlayer;

        try {
            attackedPlayer = WorldManager.getWorld(0).getPlayer(playerIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (attackingPlayer == null || attackedPlayer == null) {
            return;
        }

        /*
            Handle when a player can be attacked
         */
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == Opcodes.ATTACK_PLAYER;
    }
}
