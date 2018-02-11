package net.impl.decoder;

import net.buffers.InputBuffer;
import net.impl.events.NetworkReadEvent;
import net.impl.session.Client;
import net.packets.incoming.IncomingPacket;
import world.WorldManager;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePacketDecoder implements ProtocolDecoder {
    @Override
    public void decode(Client c) {

        int decodedOp = (c.getInBuffer().get() & 0xFF) - c.getInCipher().getNextValue();
        int packetSize = IncomingPacket.getPacketSizeForId(decodedOp);

        if (packetSize != -1 && c.getInBuffer().remaining() < packetSize) {
            c.getInBuffer().rewind();
            return;
        }

        InputBuffer in = new InputBuffer(c.getInBuffer(), packetSize);
        Optional<IncomingPacket> p = IncomingPacket.getForId(decodedOp);

        if (p.isPresent()) {
            WorldManager.submitTask(c.getPlayer().getWorldId(), () -> {
                try {
                    p.get().handle(c, decodedOp, in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            Logger.getLogger(NetworkReadEvent.class.getName()).log(Level.INFO, "Unhandled packet received : " + decodedOp + " from client: " + c.getRemoteAddress().getHostName());
        }
    }
}
