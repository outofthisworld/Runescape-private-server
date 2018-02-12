package net.impl.events;

import net.impl.session.Client;

public class NetworkReadEvent extends NetworkEvent {

    @Override
    public void accept(Client client) {
        client.readInBuffer();
        client.getProtocolDecoder().decode(client);
    }
}
