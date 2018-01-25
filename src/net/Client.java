package net;

import net.packets.IncomingPacket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Client {
    private final SocketChannel socket;
    private final SelectionKey selectionKey;
    private ByteBuffer inBuffer;
    private static final int MAX_IN_BUFFER_SIZE = 8500;
    private static final int INITIAL_IN_BUFFER_SIZE = 1024;
    private static final int HEADER_SIZE_BYTES = 5;

    public Client(SelectionKey selectionKey) {
        this.socket = (SocketChannel) selectionKey.channel();
        this.selectionKey = selectionKey;
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    void processRead() throws IOException {
        if (inBuffer == null) {
            inBuffer = ByteBuffer.allocate(INITIAL_IN_BUFFER_SIZE);
        }

        int bytesRead = getSocket().read(inBuffer);

        //1 byte opcode four byte for message length
        if (bytesRead <= HEADER_SIZE_BYTES) {
            //Here should be safe to just return because nothing has been taken from inBuffer
            return;
        }

        //Make buffer readable
        inBuffer.flip();

        //Read unsigned byte opcode
        int opCode = inBuffer.get() & 0xFF;
        //Read unsigned int length
        int len = inBuffer.getInt();

        //Malformed packet, clear the current input buffer
        if (len <= 0 || len >= MAX_IN_BUFFER_SIZE) {
            inBuffer.clear();
            return;
        }

        //If the length of the packet is higher than current inBuffers capacity
        if (len > inBuffer.capacity()) {
            //Allocate a new buffer at len bytes where 0 > lenBytes < MAX_IN_BUFFER_SIZE
            ByteBuffer b = ByteBuffer.allocate(len + HEADER_SIZE_BYTES);
            inBuffer.rewind();
            //copy the bytes currently in the inBuffer to the new buffer
            b.put(inBuffer);
            //Clear the current in buffer.
            inBuffer.clear();
            //Set inBuffer to the newly created buffer
            inBuffer = b;
            //Attempt to read remaining len bytes required....
            bytesRead += getSocket().read(inBuffer);
            //Flip the buffer (set position zero and limit to position)
            inBuffer.flip();
            //Skip the header
            inBuffer.position(HEADER_SIZE_BYTES);
        }

        //If we dont have len bytes to read required by this data
        if (inBuffer.limit() - HEADER_SIZE_BYTES < len) {
            //Make the buffer ready for writing again
            inBuffer.limit(inBuffer.capacity());
            inBuffer.position(HEADER_SIZE_BYTES);
        } else {
            byte[] packetBytes = new byte[len];
            inBuffer.get(packetBytes, 0, packetBytes.length);
            System.out.println(new String(packetBytes));

            new IncomingPacket(this,opCode,packetBytes);
            inBuffer.limit(inBuffer.capacity());
        }
    }
}