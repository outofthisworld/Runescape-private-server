package net;

import net.packets.IncomingPacket;

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
    private final long sessionKey;
    private final long connectedAt;
    private LoginStage loginStage = LoginStage.STAGE_1;
    private ByteBuffer inBuffer;
    private OutputBuffer outBuffer = OutputBuffer.create();

    public Client(SelectionKey selectionKey) throws IOException {
        socket = (SocketChannel) selectionKey.channel();
        this.selectionKey = selectionKey;
        remoteAddress = (InetSocketAddress) getSocket().getRemoteAddress();
        sessionKey = ((long) (java.lang.Math.random() * 99999999D) << 32) + (long) (java.lang.Math.random() * 99999999D);
        connectedAt = System.nanoTime();
    }

    private SocketChannel getSocket() {
        return socket;
    }

    private SelectionKey getSelectionKey() {
        return selectionKey;
    }

    /*
        Flushes
    */
    public int flush(FlushMode flushMode) {
        int bytesWritten;
        try {
            if (flushMode == FlushMode.CHUNKED) {
                bytesWritten = outBuffer.pipeTo(socket);
            } else {
                bytesWritten = outBuffer.pipeAllTo(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleDisconnect();
            return -1;
        }
        return bytesWritten;
    }

    public long getSessionKey() {
        return sessionKey;
    }

    public long getConnectedAt() {
        return connectedAt;
    }

    public OutputBuffer outBuffer() {
        return outBuffer;
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
            if (selectionKey != null) {
                selectionKey.cancel();
            }
            if (inBuffer != null) {
                inBuffer.clear();
            }
            if (outBuffer != null) {
                outBuffer.clear();
            }
            inBuffer = null;
            outBuffer = null;
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return loginStage == LoginStage.AUTHENTICATED;
    }

    void processRead() {
        if (inBuffer == null) {
            inBuffer = ByteBuffer.allocate(Client.INITIAL_IN_BUFFER_SIZE);
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
        if (bytesRead <= Client.HEADER_SIZE_BYTES) {
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
        if (len <= 0 || len >= Client.MAX_IN_BUFFER_SIZE) {
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
        if (inBuffer.remaining() - Client.HEADER_SIZE_BYTES < len) {
            inBuffer.rewind();
        } else {
            byte[] packetBytes = new byte[len];

            try {
                inBuffer.get(packetBytes, 0, packetBytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            IncomingPacket packet = new IncomingPacket(this, opCode, packetBytes);
            if (!isLoggedIn()) {
                switch (loginStage) {
                    case STAGE_1:
                        if (packet.getInputBuffer() == null) {
                            return;
                        }

                        if (packet.getInputBuffer().remaining() != 2) {
                            System.out.println("Not enough in buffer");
                            return;
                        }

                        if (packet.getInputBuffer().readUnsignedByte() != 14) {
                            System.out.println("invalid login byte / supposed to be 14");
                            return;
                        }

                        short namepart = packet.getInputBuffer().readUnsignedByte();

                        outBuffer().writeBytes(0, 8L);
                        outBuffer.writeBigQWORD(sessionKey);
                        flush(FlushMode.ALL);
                        loginStage = LoginStage.STAGE_2;
                        break;
                    case STAGE_2:
                        int loginType = packet.getInputBuffer().readUnsignedByte();
                        if (loginType != 16 && loginType != 18) {
                            System.out.println("Wrong login type");
                            return;
                        }
                        short loginPacketSize = packet.getInputBuffer().readUnsignedByte();
                        int loginEncryptedPacketSize = loginPacketSize - (36 + 1 + 1 + 2);

                        if (loginEncryptedPacketSize <= 0) {
                            System.out.println("Zero RSA packet size");
                            return;
                        }
                        loginStage = LoginStage.STAGE_3;
                        break;
                    case STAGE_3:

                        int magicNum = packet.getInputBuffer().readUnsignedByte();
                        int revision = packet.getInputBuffer().readUnsignedByte();

                        if (magicNum != 255 || revision != 317) {
                            System.out.println("");
                            return;
                        }

                        break;
                }
                new IncomingPacket(this, opCode, packetBytes);
            } else {
                new IncomingPacket(this, opCode, packetBytes);
            }
            inBuffer.compact();
        }
    }

    private enum LoginStage {
        STAGE_1,
        STAGE_2,
        STAGE_3,
        AUTHENTICATED
    }


    public enum FlushMode {
        ALL,
        CHUNKED
    }

}