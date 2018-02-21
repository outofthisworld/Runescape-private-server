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

import net.buffers.InputBuffer;
import net.impl.session.Client;
import world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class PlayerCommandPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        int size = in.remaining();

        if (size < 1) {
            return;
        }

        Player p = c.getPlayer();
        StringTokenizer stringTokenizer = new StringTokenizer(new String(in.toArray()));

        switch (stringTokenizer.nextToken()) {
            case "coords":
                c.getOutgoingPacketBuilder()
                        .sendMessage(p.getPosition().getVector().getX() + "," + p.getPosition().getVector().getY() + "," + p.getPosition().getVector().getZ()).send();
                break;
            default:
                c.getOutgoingPacketBuilder()
                        .sendMessage("unknown command").send();
                break;
        }

    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == Opcodes.PLAYER_COMMAND;
    }

    @Override
    public Set<Integer> getOpcodes() {
        Set s = new HashSet<Integer>();
        s.add(Opcodes.PLAYER_COMMAND);
        return s;
    }
}
