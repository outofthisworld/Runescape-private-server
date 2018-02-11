package net.impl.events;

import net.impl.session.Client;

public class NetworkWriteEvent extends NetworkEvent {
    @Override
    public void accept(Client client) {
        client.writeOutgoingBuffers();
    }
}
