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

        InputBuffer in = c.getInputBuffer();

        if (in.remaining() < 1) {
            return;
        }

        int decodedOp = in.readUnsignedByte() - c.getInCipher().getNextValue() & 0xFF;
        System.out.println("decoded op = " + decodedOp);

        int packetSize = IncomingPacket.getPacketSizeForId(decodedOp);
        System.out.println(packetSize);
        if (packetSize != -1 && in.remaining() < packetSize) {
            return;
        }

        Optional<IncomingPacket> incoming = IncomingPacket.getForId(decodedOp);

        if (incoming.isPresent()) {
            WorldManager.submitTask(c.getPlayer().getWorldId(), () -> {
                try {
                    incoming.get().handle(c, decodedOp, in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            Logger.getLogger(NetworkReadEvent.class.getName()).log(Level.INFO, "Unhandled packet received : " + decodedOp + " from client: " + c.getRemoteAddress().getHostName());
        }
    }
}
