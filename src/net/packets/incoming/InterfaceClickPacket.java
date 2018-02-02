package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

public class InterfaceClickPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(InterfaceClickPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        /*if (player.getTradeSession().inTrade()) {
            player.getTradeSession().resetTrade(true);
        }

        player.getPacketBuilder().closeWindows();*/
    }


    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 130;
    }
}
