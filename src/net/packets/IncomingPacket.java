package net.packets;

import net.Client;

import java.nio.ByteBuffer;

public class IncomingPacket implements Incoming {
    private final byte[] messageBytes;
    private final Client c;
    private final int opcode;

    public IncomingPacket(Client c, int packetOpcode, byte messageBytes[]){
        this.c = c;
        this.opcode = packetOpcode;
        this.messageBytes = messageBytes;
    }
    //Decoder
    //Stream -

    @Override
    public Client getClient() {
        return c;
    }

    @Override
    public int getOpcode() {
        return opcode;
    }

    @Override
    public byte[] getPacketBytes() {
        return messageBytes;
    }
}
