package net.impl;

import java.nio.ByteBuffer;

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

        client.getProtocolDecoder().decode(client);
        client.getInBuffer().compact();
    }
}
