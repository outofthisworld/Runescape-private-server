package net.packets;

import net.Client;
import net.InputBuffer;

public class IncomingPacket implements Incoming {
    private final byte[] messageBytes;
    private final Client c;
    private final int opcode;

    public IncomingPacket(Client c, int packetOpcode, byte[] messageBytes) {
        this.c = c;
        opcode = packetOpcode;
        this.messageBytes = messageBytes;
    }


    //OutputBuffer -

    @Override
    public Client getClient() {
        return c;
    }

    @Override
    public int getOpcode() {
        return opcode;
    }

    @Override
    public InputBuffer getInputBuffer() {
        return new InputBuffer(messageBytes);
    }
}
