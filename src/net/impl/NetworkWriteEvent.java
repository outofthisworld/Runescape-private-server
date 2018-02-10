package net.impl;

import net.buffers.OutputBuffer;

import java.util.concurrent.CompletableFuture;

public class NetworkWriteEvent extends NetworkEvent {
    @Override
    public void accept(Client client) {
        CompletableFuture.runAsync(() -> {
            OutputBuffer buf;

            while ((buf = client.getOutgoingBuffers().poll()) != null) {
                client.writeOutBuf(buf, false);
            }
        });
    }
}
