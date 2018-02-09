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

package net.network;

import net.buffers.OutputBuffer;
import net.enc.ISAACCipher;
import net.packets.outgoing.OutgoingPacketBuilder;
import world.entity.player.Player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;


/**
 * The type Client.
 */
public class Client implements NetworkEventExecutor {
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

    protected SocketChannel getChannel() {
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
     * Gets in buffer.
     *
     * @return the in buffer
     */
    protected ByteBuffer getInBuffer() {
        return inBuffer;
    }

    /**
     * Sets in buffer.
     *
     * @param inBuffer the in buffer
     */
    protected void setInBuffer(ByteBuffer inBuffer) {
        this.inBuffer = inBuffer;
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
     * Gets outgoing buffers.
     *
     * @return the outgoing buffers
     */
    protected ConcurrentLinkedDeque<OutputBuffer> getOutgoingBuffers() {
        return outgoingBuffers;
    }

    /**
     * Write out buf int.
     *
     * @param outBuffer the out buffer
     * @param isNew     the is new
     * @return the int
     */
    protected int writeOutBuf(OutputBuffer outBuffer, boolean isNew) {
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


    /**
     * Gets remote address.
     *
     * @return the remote address
     */
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
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


    @Override
    public void execute(NetworkEvent event) {
        event.accept(this);
    }
}

