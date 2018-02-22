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
import world.event.impl.RegionUpdateEvent;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

public class PlayerCommandPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private StringBuilder messageBuilder = new StringBuilder();


    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        int size = in.remaining();

        if (size < 1) {
            return;
        }

        Player p = c.getPlayer();
        Scanner scanner = new Scanner(new String(in.toArray()));
        scanner.useDelimiter(" ");

        if (!scanner.hasNext()) return;

        String input = scanner.next().toLowerCase().trim();
        switch (input) {
            case "tele":

                int count = 0;
                while (scanner.hasNextInt()) {
                    count++;
                }

                if (count < 2) {
                    c.getOutgoingPacketBuilder()
                            .sendMessage("Invalid command format, usage - ::tele x y [z]").send();
                }

                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int z = 0;

                if (count == 3) z = scanner.nextInt();

                p.getPosition().getVector().setX(x).setY(y).setZ(z);
                p.getWorld().getEventBus().fire(new RegionUpdateEvent(p, null));
                break;
            case "coords":
                c.getOutgoingPacketBuilder()
                        .sendMessage(messageBuilder
                                .append(p.getPosition().getVector().getX())
                                .append(",").append(p.getPosition().getVector().getY())
                                .append(",").append(p.getPosition().getVector().getZ())
                                .toString())
                        .send();
                break;
            case "local":
                c.getOutgoingPacketBuilder()
                        .sendMessage(messageBuilder
                                .append(p.getPosition().getLocalX())
                                .append(",")
                                .append(p.getPosition().getLocalY())
                                .toString())
                        .send();
                break;
            default:
                c.getOutgoingPacketBuilder()
                        .sendMessage("unknown command: " + input).send();
                break;
        }


        messageBuilder.delete(0, messageBuilder.length());
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
