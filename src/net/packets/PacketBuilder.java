package net.packets;

import net.Client;
import net.buffers.OutputBuffer;

public class PacketBuilder {

    private final OutputBuffer outputBuffer = OutputBuffer.create();
    private final Client c;

    public PacketBuilder(Client c) {
        this.c = c;
    }


    public PacketBuilder logout() {
        if (c.getPlayer().save()) {
            outputBuffer.writeByte(109 + c.getOutCipher().getNextValue());
        }
        return this;
    }


    public void clear() {

    }

    public void send() {
        c.write(outputBuffer);
    }
}
