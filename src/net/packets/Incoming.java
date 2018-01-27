package net.packets;

import net.Client;
import net.InputBuffer;

public interface Incoming {
    Client getClient();

    int getOpcode();

    InputBuffer getInputBuffer();
}
