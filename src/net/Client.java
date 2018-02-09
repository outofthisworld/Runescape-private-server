/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package net;

import net.buffers.InputBuffer;
import net.buffers.OutputBuffer;
import net.enc.ISAACCipher;
import net.packets.incoming.IncomingPacket;
import net.packets.outgoing.OutgoingPacketBuilder;
import world.WorldManager;
import world.entity.player.Player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The type Client.
 */
public class Client {
    private static final int MAX_IN_BUFFER_SIZE = 8500, MAX_PACKET_SIZE = 512, BUFFER_INCREASE_SIZE = 1024;
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private final SocketChannel channel;
    private final SelectionKey selectionKey;
    private final InetSocketAddress remoteAddress;
    private final long serverSessionKey, connectedAt;
    private final Date lastConnectionDate = new Date();
    private final ConcurrentLinkedDeque<OutputBuffer> outgoingBuffers = new ConcurrentLinkedDeque<>();
    private final OutgoingPacketBuilder outgoingPacketBuilder = new OutgoingPacketBuilder(this);
    private long disconnectedAt = -1;
    private boolean isDisconnected = false;
    private ByteBuffer inBuffer;
    private ISAACCipher inCipher;
    private ISAACCipher outCipher;
    private volatile boolean isLoggedIn;
    private Player player;


    /**
     * Instantiates a new Client.
     *
     * @param selectionKey the selection key
     * @throws IOException the io exception
     */
    public Client(SelectionKey selectionKey) throws IOException {
        channel = (SocketChannel) selectionKey.channel();
        this.selectionKey = selectionKey;
        remoteAddress = (InetSocketAddress) getChannel().getRemoteAddress();
        serverSessionKey = ((long) (java.lang.Math.random() * 99999999D) << 32) + (long) (java.lang.Math.random() * 99999999D);
        connectedAt = System.nanoTime();
    }

    private SocketChannel getChannel() {
        return channel;
    }

    private SelectionKey getSelectionKey() {
        return selectionKey;
    }

    /**
     * Gets in cipher.
     *
     * @return the in cipher
     */
    public ISAACCipher getInCipher() {
        return inCipher;
    }

    /**
     * Set in cipher.
     *
     * @param cipher the cipher
     */
    public void setInCipher(ISAACCipher cipher) {
        if (inCipher != null) {
            throw new IllegalStateException("Cipher for client already set");
        }
        inCipher = cipher;
    }

    /**
     * Is logged in boolean.
     *
     * @return the boolean
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Sets logged in.
     *
     * @param loggedIn the logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    /**
     * Gets out cipher.
     *
     * @return the out cipher
     */
    public ISAACCipher getOutCipher() {
        return outCipher;
    }

    /**
     * Set out cipher.
     *
     * @param cipher the cipher
     */
    public void setOutCipher(ISAACCipher cipher) {
        if (outCipher != null) {
            throw new IllegalStateException("Cipher for client already set");
        }
        outCipher = cipher;
    }

    /**
     * Gets disconnected at.
     *
     * @return the disconnected at
     */
    public long getDisconnectedAt() {
        return disconnectedAt;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param p the p
     */
    public void setPlayer(Player p) {
        player = p;
    }

    /**
     * Process write.
     *
     * @return the completable future
     */
    CompletableFuture processWrite() {
        return CompletableFuture.runAsync(() -> {
            OutputBuffer buf;

            while ((buf = outgoingBuffers.poll()) != null) {
                writeOutBuf(buf, false);
            }
        });
    }


    private int writeOutBuf(OutputBuffer outBuffer, boolean isNew) {
        int bytesWritten = 0;

        int outBufSize = outBuffer.size();

        if (outgoingBuffers.size() == 0) {
            try {
                bytesWritten = outBuffer.pipeTo(channel);
            } catch (IOException e) {
                e.printStackTrace();
                disconnect();
            }
        }

        if (bytesWritten == -1) {
            disconnect();
            return -1;
        }

        if (bytesWritten == 0 || bytesWritten != outBufSize) {
            if (isNew) {
                outgoingBuffers.addLast(outBuffer);
            } else {
                outgoingBuffers.addFirst(outBuffer);
            }
            selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
        }

        return bytesWritten;
    }

    /**
     * Thread safe, attempts to write the specified OutputBuffer to the client,
     * if the socketchannels buffer is full, register OP_WRITE.
     *
     * @param outBuffer the out buffer
     * @return the int
     */
    public CompletableFuture<Integer> write(OutputBuffer outBuffer) {
        return CompletableFuture.supplyAsync(() -> writeOutBuf(outBuffer, true));
    }

    /**
     * Gets packet builder.
     *
     * @return the packet builder
     */
    public OutgoingPacketBuilder getOutgoingPacketBuilder() {
        return outgoingPacketBuilder;
    }


    /**
     * Gets server session key.
     *
     * @return the server session key
     */
    public long getServerSessionKey() {
        return serverSessionKey;
    }

    /**
     * Gets connected at.
     *
     * @return the connected at
     */
    public long getConnectedAt() {
        return connectedAt;
    }


    private int readInBuf() throws Exception {

        //Attempt to resize the input buffer so inBuffer.remaining() is always at-least max packet size.
        //Throw an exception if inBuffer.capacity() + BUFFER_INCREASE_SIZE >= MAX_IN_BUFFER_SIZE.
        if (inBuffer.remaining() < Client.MAX_PACKET_SIZE) {
            if (inBuffer.capacity() + Client.BUFFER_INCREASE_SIZE >= Client.MAX_IN_BUFFER_SIZE) {
                throw new Exception("InBuffer reached maximum");
            } else {
                ByteBuffer b = ByteBuffer.allocate(inBuffer.capacity() + Client.BUFFER_INCREASE_SIZE);
                inBuffer.flip();
                b.put(inBuffer);
                inBuffer.clear();
                inBuffer = b;
            }
        }

        int bytesRead = getChannel().read(inBuffer);
        if (bytesRead == -1) {
            throw new Exception("end of stream reached");
        }

        return bytesRead;
    }

    /**
     * Handle disconnect.
     */
    public void disconnect() {
        if (isDisconnected) {
            return;
        }

        outgoingBuffers.clear();

        //logger.log(Level.INFO, String.format("Client disconnect: $s : %d", this.remoteAddress.getHostString(), this.remoteAddress.getPort()));

        if (selectionKey != null) {
            selectionKey.cancel();
        }
        if (inBuffer != null) {
            inBuffer.clear();
        }
        inBuffer = null;
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        disconnectedAt = System.nanoTime();
        isDisconnected = true;
    }

    /**
     * Is disconnected boolean.
     *
     * @return the boolean
     */
    public boolean isDisconnected() {
        return isDisconnected;
    }


    /**
     * Process read.
     */
    void processRead() {
        if (inBuffer == null) {
            inBuffer = ByteBuffer.allocate(Client.BUFFER_INCREASE_SIZE);
        }

        int bytesRead;

        try {
            bytesRead = readInBuf();
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
            return;
        }

        //Make buffer readable
        inBuffer.flip();

        if (inBuffer.remaining() < 1) {
            inBuffer.compact();
            return;
        }

        int op = inBuffer.get() & 0xFF;

        Client c = this;
        if (!isLoggedIn()) {

            try {
                LoginDecoder.login(c, op, new InputBuffer(inBuffer));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            int decodedOp = op - inCipher.getNextValue();
            int packetSize = IncomingPacket.getPacketSizeForId(decodedOp);

            if (packetSize != -1 && inBuffer.remaining() < packetSize) {
                inBuffer.rewind();
                inBuffer.compact();
                return;
            }

            InputBuffer in = new InputBuffer(inBuffer, packetSize);
            Optional<IncomingPacket> p = IncomingPacket.getForId(op);

            if (p.isPresent()) {
                WorldManager.submitTask(0, () -> {
                    try {
                        p.get().handle(this, decodedOp, in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Client.logger.log(Level.INFO, "Unhandled packet received : " + op + " from client: " + remoteAddress.getHostName());
            }
        }
        inBuffer.compact();
    }
}

