package net.impl.decoder;

import net.buffers.InputBuffer;
import net.impl.NetworkConfig;
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

        int encodedOp = (in.readSignedByte() & 0xFF);
        int decodedOp =  encodedOp - c.getInCipher().getNextValue() & 0xFF;

        int packetSize = IncomingPacket.getPacketSizeForId(decodedOp);


        if(packetSize == -1){

            if(in.remaining() < 1)
                return;

            packetSize = in.readUnsignedByte();
        }

        System.out.println("Received packet: {deocdedOpcode:"+decodedOp+",packetSize:"+packetSize+"}");

        if (in.remaining() < packetSize) {
            in.rewind();
            return;
        }

        Optional<IncomingPacket> incoming = IncomingPacket.getForId(decodedOp);
        InputBuffer payload = new InputBuffer(c.getInputBuffer(), packetSize);

        if (incoming.isPresent()) {
            WorldManager.submitTask(c.getPlayer().getWorldId(), () -> {
                try {
                    incoming.get().handle(c, decodedOp, payload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            Logger.getLogger(NetworkReadEvent.class.getName()).log(Level.INFO, "Unhandled packet received : " + decodedOp + " from client: " + c.getRemoteAddress().getHostName());
        }
    }
}
