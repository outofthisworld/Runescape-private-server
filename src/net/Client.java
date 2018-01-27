package net;

import net.packets.IncomingPacket;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Client {
    private static final int MAX_IN_BUFFER_SIZE = 8500;
    private static final int INITIAL_IN_BUFFER_SIZE = 1024;
    private static final int HEADER_SIZE_BYTES = 5;
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private final SocketChannel socket;
    private final SelectionKey selectionKey;
    private final InetSocketAddress remoteAddress;
    private ByteBuffer inBuffer;
    private OutputBuffer outBuffer = OutputBuffer.create();

    public Client(SelectionKey selectionKey) throws IOException {
        this.socket = (SocketChannel) selectionKey.channel();
        this.selectionKey = selectionKey;
        this.remoteAddress = (InetSocketAddress) getSocket().getRemoteAddress();
    }

    private SocketChannel getSocket() {
        return socket;
    }

    private SelectionKey getSelectionKey() {
        return selectionKey;
    }


    public enum FlushMode{
        ALL,
        CHUNKED
    }

    /*
        Flushes
    */
    public int flush(FlushMode flushMode) {
        int bytesWritten;
        try {
            if(flushMode == FlushMode.CHUNKED) {
                bytesWritten = outBuffer.pipeTo(this);
            }else{
                bytesWritten = outBuffer.pipeAllTo(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleDisconnect();
            return -1;
        }
        return bytesWritten;
    }

    public OutputBuffer outBuffer(){
        return this.outBuffer;
    }

    private int readInBuf() throws Exception {
        int bytesRead = getSocket().read(inBuffer);
        if (bytesRead == -1) {
            throw new Exception("End of stream in read, is disconnected?");
        }

        return bytesRead;
    }

     void handleDisconnect() {
        //logger.log(Level.INFO, String.format("Client disconnect: $s : %d", this.remoteAddress.getHostString(), this.remoteAddress.getPort()));
        try {
            if (selectionKey != null)
                selectionKey.cancel();
            if (inBuffer != null)
                inBuffer.clear();
            if (outBuffer != null)
                outBuffer.clear();
            inBuffer = null;
            outBuffer = null;
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void processRead() {
        if (inBuffer == null) {
            inBuffer = ByteBuffer.allocate(INITIAL_IN_BUFFER_SIZE);
        }

        int bytesRead;

        try {
            bytesRead = readInBuf();
        } catch (Exception e) {
            e.printStackTrace();
            handleDisconnect();
            return;
        }
        //1 byte opcode four byte for message length
        if (bytesRead <= HEADER_SIZE_BYTES) {
            //Here should be safe to just return because nothing has been taken from inBuffer
            return;
        }

        //Make buffer readable
        inBuffer.flip();

        //Read unsigned byte opcode
        int opCode = inBuffer.get() & 0xFF;
        //Read  int length
        int len = inBuffer.getInt();

        //Malformed packet, clear the current input buffer
        if (len <= 0 || len >= MAX_IN_BUFFER_SIZE) {
            inBuffer.clear();
            return;
        }

        //If the length of the packet is higher than current inBuffers capacity
        if (len > inBuffer.capacity()) {
            ByteBuffer newInBuffer = ByteBuffer.allocate(len);
            inBuffer.rewind();
            //Put whatevers remaining
            newInBuffer.put(inBuffer);

            inBuffer.clear();
            inBuffer = newInBuffer;

            try {
                readInBuf();
            } catch (Exception e) {
                e.printStackTrace();
                handleDisconnect();
                return;
            }

            inBuffer.flip();
        }

        //If we dont have len bytes to read required by this data
        if (inBuffer.limit() - HEADER_SIZE_BYTES < len) {
            inBuffer.rewind();
        } else {
            byte[] packetBytes = new byte[len];
            try {
                inBuffer.get(packetBytes, 0, packetBytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(new String(packetBytes));

            new IncomingPacket(this, opCode, packetBytes);
            inBuffer.limit(inBuffer.capacity());
        }

        inBuffer.compact();
    }

}