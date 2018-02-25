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


        if (!scanner.hasNext()) return;

        String input = scanner.next().toLowerCase().trim();

        System.out.println("Received player command : " + input);
        switch (input) {
            case "tele":
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int z = 0;

                if (scanner.hasNextInt()) z = scanner.nextInt();

                p.getPosition().getVector().setX(x).setY(y).setZ(z);
                p.getWorld().getEventBus().fire(new RegionUpdateEvent(p, null));
                break;
            case "item":
                int itemId = scanner.nextInt();
                int amount = scanner.nextInt();

                if(p.getInventory().add(itemId,amount)){
                    c.getOutgoingPacketBuilder()
                            .sendMessage(messageBuilder
                                    .append("Added ")
                                    .append(", remaining slots : ")
                                    .append(p.getInventory().getContainer().remaining())
                                    .toString())
                            .send();
                }else{
                    c.getOutgoingPacketBuilder()
                            .sendMessage(messageBuilder
                                    .append("Could not add item ")
                                    .append(itemId)
                                    .append(" your inventory may be too full, or the item is invalid.")
                                    .toString())
                            .send();
                }

                break;
            case "remove":


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
