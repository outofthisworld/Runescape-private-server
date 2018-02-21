package net.impl.decoder;

import net.buffers.InputBuffer;
import net.impl.session.Client;
import net.packets.incoming.IncomingPacket;
import util.Stopwatch;

import java.util.Optional;

public class GamePacketDecoder implements ProtocolDecoder {
    private final Stopwatch stopWatch = new Stopwatch();

    @Override
    public void decode(Client c) {

        InputBuffer in = c.getInputBuffer();

        while (in.remaining() > 0) {
            int encodedOp = (in.readSignedByte() & 0xFF);
            int decodedOp = encodedOp - c.getInCipher().getNextValue() & 0xFF;

            if (decodedOp == 0) {
                continue;
            }

            int packetSize = IncomingPacket.getPacketSizeForId(decodedOp);


            if (packetSize == -1) {

                if (in.remaining() < 1)
                    return;

                packetSize = in.readUnsignedByte();
            }

            if (in.remaining() < packetSize) {
                in.rewind();
                return;
            }

            Optional<IncomingPacket> incoming = IncomingPacket.getForId(decodedOp);
            InputBuffer payload = new InputBuffer(c.getInputBuffer(), packetSize);
            incoming.ifPresent(packet -> c.getPlayer().getWorld().submit(() -> {
                try {
                    packet.handle(c, decodedOp, payload);
                    stopWatch.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
    }
}
