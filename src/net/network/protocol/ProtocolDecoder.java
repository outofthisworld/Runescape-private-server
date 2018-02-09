package net.network.protocol;

import net.network.Client;

public interface ProtocolDecoder {
    void decode(Client c);
}
