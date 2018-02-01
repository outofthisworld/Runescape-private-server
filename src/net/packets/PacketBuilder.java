package net.packets;

import net.Client;
import net.buffers.OutputBuffer;

public class PacketBuilder {

    private final Client c;
    private OutputBuffer outputBuffer = OutputBuffer.create();

    public PacketBuilder(Client c) {
        this.c = c;
    }


    public PacketBuilder logout() {
        if (c.getPlayer().save()) {
            outputBuffer.writeByte(109 + c.getOutCipher().getNextValue());
        }
        return this;
    }


    public PacketBuilder sendMessage(String s) {
        outputBuffer.writeByte(253);
        byte[] sBytes = s.getBytes();
        outputBuffer.writeByte(sBytes.length);
        outputBuffer.writeBytes(sBytes);
        return this;
    }

    public OutputBuffer build() {
        OutputBuffer current = outputBuffer;
        outputBuffer = OutputBuffer.create();
        return current;
    }

    public void send() {
        c.write(build());
    }
}
