package world.event.impl;

import net.impl.session.Client;
import util.Preconditions;

public class ClientDisconnectEvent extends AbstractEvent {
    private final Client c;

    public ClientDisconnectEvent(Client c) {
        Preconditions.notNull(c);
        this.c = c;
    }

    @Override
    public Client getSender() {
        return c;
    }
}
