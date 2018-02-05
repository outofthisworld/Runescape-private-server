package net.packets.outgoing;

import net.buffers.OutputBuffer;

public class ServerMessage extends OutgoingPacket {


    /**
     * Instantiates a new Outgoing packet.
     *
     * @param out
     */
    public ServerMessage() {
        super(OutputBuffer.create(6));
        //outputBuffer.writeByte()
    }
}
