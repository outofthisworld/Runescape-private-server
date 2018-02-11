package net.impl.decoder;

import net.impl.session.Client;

public interface ProtocolDecoder {
    void decode(Client c);
}
