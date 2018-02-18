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

package net.impl.session;

import net.buffers.InputBuffer;
import net.buffers.OutputBuffer;
import net.impl.decoder.LoginSessionDecoder;
import net.impl.decoder.ProtocolDecoder;
import net.impl.enc.ISAACCipher;
import net.impl.events.NetworkEvent;
import net.impl.events.NetworkEventExecutor;
import net.packets.outgoing.OutgoingPacket;
import net.packets.outgoing.OutgoingPacketBuilder;
import world.entity.player.Player;
import world.event.impl.ClientDisconnectEvent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferOverflowException;
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
    private final InputBuffer inputBuffer;
    private long disconnectedAt = -1;
    private boolean isDisconnected = false;
    private ISAACCipher inCipher;
    private ISAACCipher outCipher;
    private Player player;
    private ProtocolDecoder protocolDecoder = new LoginSessionDecoder();

    /**
     * Instantiates a new Client.
     *
     * @param key the key
     * @throws IOException the io exception
     */
    public Client(SelectionKey key) throws IOException {
        selectionKey = key;
        connectedAt = System.nanoTime();
        inputBuffer = new InputBuffer();
        channel = (SocketChannel) selectionKey.channel();
        remoteAddress = (InetSocketAddress) getChannel().getRemoteAddress();
        serverSessionKey = ((long) (java.lang.Math.random() * 99999999D) << 32) + (long) (java.lang.Math.random() * 99999999D);
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    private SocketChannel getChannel() {
        return channel;
    }

    /**
     * Gets protocol decoder.
     *
     * @return the protocol decoder
     */
    public ProtocolDecoder getProtocolDecoder() {
        return protocolDecoder;
    }

    /**
     * Sets protocol decoder.
     *
     * @param protocolDecoder the protocol decoder
     */
    public void setProtocolDecoder(ProtocolDecoder protocolDecoder) {
        this.protocolDecoder = protocolDecoder;
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
     * Gets input buffer.
     *
     * @return the input buffer
     */
    public InputBuffer getInputBuffer() {
        return inputBuffer;
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
    private ConcurrentLinkedDeque<OutputBuffer> getOutgoingBuffers() {
        return outgoingBuffers;
    }


    /**
     * Read in buffer int.
     *
     * @return the int
     */
    public int readInBuffer() {
        int bytesRead = 0;
        Exception ex = null;
        try {
            bytesRead = getInputBuffer().readFrom(channel);
        } catch (BufferOverflowException e) {
            e.printStackTrace();
            ex = e;
        } catch (IOException e) {
            e.printStackTrace();
            ex = e;
        }

        if (ex != null || bytesRead == -1) {
            disconnect();
            return -1;
        }

        return bytesRead;
    }


    /**
     * Write out buf int.
     *
     * @param outBuffer the out buffer
     * @param isNew     the is new
     * @return the int
     */
    private int write(OutputBuffer outBuffer, boolean isNew) {
        int outBufSize = outBuffer.size();

        if (outBufSize <= 0) {
            return 0;
        }

        int bytesWritten = 0;

        if (!isNew || outgoingBuffers.size() == 0) {
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
        if (outBuffer.size() == 0) {
            return CompletableFuture.completedFuture(0);
        }
        return CompletableFuture.supplyAsync(() -> write(outBuffer, true));
    }

    /**
     * Write completable future.
     *
     * @param packet the packet
     * @return the completable future
     */
    public CompletableFuture<Integer> write(OutgoingPacket packet) {
        return write(packet.toOutputBuffer());
    }


    /**
     * Output buffers are queued if the socket channels internal buffer is full
     * When this happens, socketchannel.write() returns 0 bytes written, even though
     * the sent buffer may have more than zero bytes in it.
     * <p>
     * As such, we register this clients selection key with the writable event
     * which will in turn call this method when the socket channels underlying buffer has
     * available bytes.
     */
    public void writeOutgoingBuffers() {
        CompletableFuture.runAsync(() -> {
            //Deregister for write events
            selectionKey.interestOps(selectionKey.interestOps() & (~SelectionKey.OP_WRITE));
            //Write the queued outgoing buffers, if the outgoing buffers are not all written write will
            //register the selection key for write events.
            OutputBuffer buf;
            while ((buf = getOutgoingBuffers().poll()) != null) {
                if (buf.size() == 0) {
                    continue;
                }
                write(buf, false);
            }
        });
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

        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outgoingBuffers.clear();
        selectionKey.cancel();
        inputBuffer.clear();
        disconnectedAt = System.nanoTime();
        isDisconnected = true;
        player.getWorld().getEventBus().fire(new ClientDisconnectEvent(this));
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

