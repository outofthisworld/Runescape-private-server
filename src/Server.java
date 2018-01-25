package net;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Server {
    private ServerSocketChannel serverSocketChannel;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private InetSocketAddress address;
    private volatile boolean isRunning = false;
    private Selector onAcceptableSelector;
    private ConnectionHandler connectionHandlers[];
    private ExecutorService service;
    private int numConnectionHandlers;

    public Server(String host, int port) {
        this(host, port, Runtime.getRuntime().availableProcessors() - 1);
    }

    public Server(String host, int port, int numConnectionHandlers) {
        address = new InetSocketAddress(host, port);
        if (numConnectionHandlers < 1 || numConnectionHandlers >= 50) {
            throw new IllegalArgumentException("Invalid numConnection handlers");
        }
        this.numConnectionHandlers = numConnectionHandlers;
    }


    private final void initConnectionHandlers() throws IOException {
        if (connectionHandlers == null) {
            connectionHandlers = new ConnectionHandler[numConnectionHandlers];
        } else {
            throw new InvalidStateException("Connection handlers was set on start");
        }

        if (service == null) {
            service = Executors.newFixedThreadPool(connectionHandlers.length);
        } else {
            throw new InvalidStateException("service already existed when attempting to start server");
        }

        for (int i = 0; i < connectionHandlers.length; i++) {

            connectionHandlers[i] = new ConnectionHandler();

            if (service.isShutdown() || service.isTerminated()) {
                throw new InvalidStateException("Connection handler executor service is shutdown");
            }

            service.submit(connectionHandlers[i]);
        }
    }

    private final void bindServerSocketChannel() throws IOException {
        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            serverSocketChannel.close();
            serverSocketChannel = null;
        }
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(address);
    }

    private final void registerServerSelector() throws IOException {
        if (onAcceptableSelector != null && onAcceptableSelector.isOpen()) {
            onAcceptableSelector = null;
            onAcceptableSelector.close();
            ;
        }
        onAcceptableSelector = Selector.open();
        serverSocketChannel.register(onAcceptableSelector, SelectionKey.OP_ACCEPT);
    }

    /*

        Binds the server to a new address (requires restarting server)
     */
    public void setAddress(InetSocketAddress address) throws IOException {
        stop();
        this.address = address;
        start();
    }

    public void start() throws IOException {
        if (serverSocketChannel != null && serverSocketChannel.isOpen()) return;

        logger.log(Level.INFO, String.format("Starting server on host: %s using port : %d", getINetAddress().getHostName(), getINetAddress().getPort()));

        //Bind the server
        bindServerSocketChannel();
        //Register the serverSocketChannel with the servers selector
        registerServerSelector();
        //Init connection handlers to handle reading
        initConnectionHandlers();

        isRunning = true;

        logger.log(Level.INFO, "Successfully binded server");

        while (isRunning) {
            try {
                onAcceptableSelector.select(5000);
                Set<SelectionKey> selectionKeySet = onAcceptableSelector.selectedKeys();

                SelectionKey currentlySelected;
                for (Iterator<SelectionKey> it = selectionKeySet.iterator(); it.hasNext(); ) {
                    currentlySelected = it.next();

                    if (currentlySelected == null) continue;

                    if (!currentlySelected.isValid()) {
                        it.remove();
                        continue;
                    }

                    if (currentlySelected.isAcceptable()) {

                        SocketChannel socketChannel = serverSocketChannel.accept();

                        //Check we allow this connection
                        SocketGateway.accept(socketChannel);

                        if (socketChannel != null && socketChannel.isConnected()) {
                            socketChannel.finishConnect();
                            connectionHandlers[(int) Math.random() * connectionHandlers.length].handle(socketChannel);
                        }
                    }

                    it.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        stop();
    }

    public void stop() {
        if (!isRunning) return;

        isRunning = false;

        if (service != null && !service.isShutdown())
            service.shutdown();

        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocketChannel = null;

        try {
            onAcceptableSelector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        onAcceptableSelector = null;

        for (int i = 0; i < connectionHandlers.length; i++) {
            connectionHandlers[i] = null;
        }

        connectionHandlers = null;

        try {
            service.awaitTermination(5L, TimeUnit.SECONDS);
            service = null;
        } catch (InterruptedException e) {
            service = null;
            return;
        }
    }

    private static final class SocketGateway {

        static {

        }

        public static void accept(SocketChannel socketChannel) {
            //Close socketChannel if we don't accept it
        }
    }


    public InetSocketAddress getINetAddress() {
        return address;
    }

}


