package net;

import net.impl.NetworkConfig;
import net.impl.channel.ChannelGateway;
import net.impl.channel.ChannelManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type net.Reactor.
 */
public final class Reactor {
    private static final Logger logger = Logger.getLogger(Reactor.class.getName());
    private static final int MAX_INCOMING_THRESHOLD = 10;
    private final ChannelManager channelManager = ChannelManager.create();
    private final int numConnectionHandlers;
    private ServerSocketChannel serverSocketChannel;
    private InetSocketAddress address;
    private Selector onAcceptableSelector;

    /**
     * Instantiates a new net.Reactor.
     *
     * @param host the host
     * @param port the port
     */
    public Reactor(String host, int port) {
        this(host, port, NetworkConfig.DEFAULT_NO_CHANNEL_HANDLERS);
    }

    /**
     * Instantiates a new net.Reactor.
     */
    public Reactor() {
        this(NetworkConfig.HOST, NetworkConfig.PORT, NetworkConfig.DEFAULT_NO_CHANNEL_HANDLERS);
    }

    /**
     * Instantiates a new net.Reactor.
     *
     * @param host                  the host
     * @param port                  the port
     * @param numConnectionHandlers the num connection handlers
     */
    public Reactor(String host, int port, int numConnectionHandlers) {
        address = new InetSocketAddress(host, port);
        if (numConnectionHandlers < 1 || numConnectionHandlers >= 50) {
            throw new IllegalArgumentException("Invalid numConnection handlers");
        }
        this.numConnectionHandlers = numConnectionHandlers;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    /**
     * Bind channel.
     */
    private void bindServerSocketChannel() throws IOException {
        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            serverSocketChannel.close();
            serverSocketChannel = null;
        }
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(address);
    }

    /**
     * Bind channel.
     */
    private void registerServerSelector() throws IOException {
        if (onAcceptableSelector != null && onAcceptableSelector.isOpen()) {
            onAcceptableSelector.close();
            onAcceptableSelector = null;
        }
        onAcceptableSelector = Selector.open();
        serverSocketChannel.register(onAcceptableSelector, SelectionKey.OP_ACCEPT);
    }

    /**
     * Start.
     *
     * @throws IOException the io exception
     */
    public void start() throws IOException {
        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            return;
        }

        Reactor.logger.log(Level.INFO, String.format("Starting server on host: %s using port : %d", getAddress().getHostName(), getAddress().getPort()));

        //Bind the server
        bindServerSocketChannel();
        //Register the serverSocketChannel with the servers selector
        registerServerSelector();
        //Init connection handlers to handle reading


        Reactor.logger.log(Level.INFO, "Successfully binded server");

        while (serverSocketChannel.isOpen() && onAcceptableSelector.isOpen()) {
            acceptConnections();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stop();
    }

    /**
     * Accept.
     */
    private void acceptConnections() {
        try {
            onAcceptableSelector.select(300);
            Set<SelectionKey> selectionKeySet = onAcceptableSelector.selectedKeys();

            SelectionKey currentlySelected;
            int incomingAccepted = 0;

            for (Iterator<SelectionKey> it = selectionKeySet.iterator(); it.hasNext(); ) {

                if (incomingAccepted >= MAX_INCOMING_THRESHOLD) {
                    break;
                }

                currentlySelected = it.next();

                if (currentlySelected == null) {
                    continue;
                }

                if (!currentlySelected.isValid()) {
                    it.remove();
                    continue;
                }

                if (currentlySelected.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    Reactor.logger.log(Level.INFO, "Accepted connection, registering with channel manager");
                    if (socketChannel != null && socketChannel.isConnected()) {

                        //Check we allow this connection
                        ChannelGateway.accept(socketChannel);
                        channelManager.register(socketChannel);
                        incomingAccepted++;
                    }
                }

                it.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop.
     */
    public void stop() {

        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        serverSocketChannel = null;

        if (onAcceptableSelector != null) {
            try {
                onAcceptableSelector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            channelManager.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        onAcceptableSelector = null;
    }

    /**
     * Gets i net address.
     *
     * @return the i net address
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     * @throws IOException the io exception
     */
/*

        Binds the server to a new address (requires restarting server)
     */
    public void setAddress(InetSocketAddress address) throws IOException {
        stop();
        this.address = address;
        start();
    }
}


