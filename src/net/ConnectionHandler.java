package net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ConnectionHandler implements Runnable {
    private final Selector selector;

    public ConnectionHandler() throws IOException {
        this.selector = Selector.open();
    }

    public void handle(SocketChannel socketChannel) throws Exception {
        if (!socketChannel.isConnected()) {
            throw new Exception("ConnectionHandler.java: Attempted to handle an unconnected socket channel");
        }

        if (socketChannel.isBlocking()) {
            socketChannel.configureBlocking(false);
        }

        System.out.println("Registering socket channel with connection handler");
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public void shutdown() {

    }

    @Override
    public void run() {
        while (selector.isOpen()) {
            try {
                selector.select(5000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pollSelections();
        }
    }

    protected final void pollSelections() {
        Set<SelectionKey> selectionKeySet = selector.selectedKeys();

        SelectionKey currentlySelected;
        for (Iterator<SelectionKey> it = selectionKeySet.iterator(); it.hasNext(); ) {

            currentlySelected = it.next();

            if (currentlySelected.isReadable()) {
                System.out.println("readble selection");
                if (currentlySelected.attachment() == null) {
                    try {
                        currentlySelected.attach(new Client(currentlySelected));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                if (currentlySelected.attachment() !=  null && currentlySelected.isValid()) {
                    Client c = (Client) currentlySelected.attachment();
                    try {
                        c.processRead();
                        //Read into read buffer...
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            it.remove();
        }
    }
}