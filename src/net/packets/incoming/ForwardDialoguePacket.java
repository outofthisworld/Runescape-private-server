package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

/**
 * Sent when the player clicks on the "Click this to continue" link to forward a
 * dialogue.
 */
public class ForwardDialoguePacket extends Packet {
    private static final Logger logger = Logger.getLogger(ForwardDialoguePacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        /*if (player.getNpcDialogue() != 0) {
            NpcDialogue.getDialogueMap().get(player.getNpcDialogue()).dialogue(player);
        } else {
            player.getPacketBuilder().closeWindows();
        }*/
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 40;
    }
}
