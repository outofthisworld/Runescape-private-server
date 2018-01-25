package net.packets;

import net.Client;

import java.nio.ByteBuffer;

public interface Incoming {
    Client getClient();
    int getOpcode();
    byte[] getPacketBytes();
}
