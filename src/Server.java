
import net.ChannelManager;
import net.SocketGateway;

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

public final class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private ServerSocketChannel serverSocketChannel;
    private InetSocketAddress address;
    private volatile boolean isRunning = false;
    private Selector onAcceptableSelector;
    private ChannelManager channelManager = ChannelManager.create();

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
                            channelManager.register(socketChannel);
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
    }

    public InetSocketAddress getINetAddress() {
        return address;
    }
}


