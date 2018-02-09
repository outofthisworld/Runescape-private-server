package net.network;

import net.buffers.InputBuffer;
import net.network.protocol.LoginDecoder;
import net.packets.incoming.IncomingPacket;
import world.WorldManager;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkReadEvent extends NetworkEvent {
    private static final int MAX_IN_BUFFER_SIZE = 8500,
            MAX_PACKET_SIZE = 512,
            BUFFER_INCREASE_SIZE = 1024;

    /**
     * Read in buf int.
     *
     * @return the int
     * @throws Exception the exception
     */
    protected int readInBuf(Client c) throws Exception {

        //Attempt to resize the input buffer so inBuffer.remaining() is always at-least max packet size.
        //Throw an exception if inBuffer.capacity() + BUFFER_INCREASE_SIZE >= MAX_IN_BUFFER_SIZE.
        if (c.getInBuffer().remaining() < NetworkReadEvent.MAX_PACKET_SIZE) {
            if (c.getInBuffer().capacity() + NetworkReadEvent.BUFFER_INCREASE_SIZE >= NetworkReadEvent.MAX_IN_BUFFER_SIZE) {
                throw new Exception("InBuffer reached maximum");
            } else {
                ByteBuffer b = ByteBuffer.allocate(c.getInBuffer().capacity() + NetworkReadEvent.BUFFER_INCREASE_SIZE);
                c.getInBuffer().flip();
                b.put(c.getInBuffer());
                c.getInBuffer().clear();
                c.setInBuffer(b);
            }
        }

        int bytesRead = c.getChannel().read(c.getInBuffer());
        if (bytesRead == -1) {
            throw new Exception("end of stream reached");
        }

        return bytesRead;
    }

    @Override
    public void accept(Client client) {
        if (client.getInBuffer() == null) {
            client.setInBuffer(ByteBuffer.allocate(NetworkReadEvent.BUFFER_INCREASE_SIZE));
        }

        int bytesRead;

        try {
            bytesRead = readInBuf(client);
        } catch (Exception e) {
            e.printStackTrace();
            client.disconnect();
            return;
        }

        //Make buffer readable
        client.getInBuffer().flip();

        if (client.getInBuffer().remaining() < 1) {
            client.getInBuffer().compact();
            return;
        }

        int op = client.getInBuffer().get() & 0xFF;


        if (!client.isLoggedIn()) {
            try {
                LoginDecoder.login(client, op, new InputBuffer(client.getInBuffer()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            int decodedOp = op - client.getInCipher().getNextValue();
            int packetSize = IncomingPacket.getPacketSizeForId(decodedOp);

            if (packetSize != -1 && client.getInBuffer().remaining() < packetSize) {
                client.getInBuffer().rewind();
                client.getInBuffer().compact();
                return;
            }

            InputBuffer in = new InputBuffer(client.getInBuffer(), packetSize);
            Optional<IncomingPacket> p = IncomingPacket.getForId(op);

            if (p.isPresent()) {
                WorldManager.submitTask(0, () -> {
                    try {
                        p.get().handle(client, decodedOp, in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Logger.getLogger(NetworkReadEvent.class.getName()).log(Level.INFO, "Unhandled packet received : " + op + " from client: " + client.getRemoteAddress().getHostName());
            }
        }
        client.getInBuffer().compact();
    }
}
