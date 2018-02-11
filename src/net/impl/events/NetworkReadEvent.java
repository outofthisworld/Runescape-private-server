package net.impl.events;

import net.impl.session.Client;

public class NetworkReadEvent extends NetworkEvent {

    @Override
    public void accept(Client client) {
        if (client.readInBuffer() == -1) {
            return;
        }

        //Make buffer readable
        client.getInBuffer().flip();

        if (client.getInBuffer().remaining() < 1) {
            client.getInBuffer().compact();
            return;
        }

        client.getProtocolDecoder().decode(client);
        client.getInBuffer().compact();
    }
}
