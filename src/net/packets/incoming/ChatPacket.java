package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;

public class ChatPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(ChatPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {

        /*
            int effects = in.readByte(false, PacketBuffer.ValueType.S);
            int color = in.readByte(false, PacketBuffer.ValueType.S);
            int chatLength = (player.getSession().getPacketLength() - 2);
            byte[] text = in.readBytesReverse(chatLength, PacketBuffer.ValueType.A);

            player.setChatEffects(effects);
            player.setChatColor(color);
            player.setChatText(text);
            player.getFlags().flag(Flag.CHAT);
        */
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 4;
    }
}
