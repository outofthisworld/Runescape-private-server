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

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The type Channel handler.
 */
public class ChannelHandler implements IChannelHandler {
    private final Selector selector;
    private volatile boolean isRunning = false;
    private HashMap<SocketChannel, SelectionKey> sockets;
    private int numChannels = 0;

    /**
     * Instantiates a new Channel handler.
     *
     * @throws IOException the io exception
     */
    public ChannelHandler() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void handle(SocketChannel socketChannel) throws Exception {
        if (!socketChannel.isConnected()) {
            throw new Exception("ChannelHandler.java: Attempted to login an unconnected socket channel");
        }

        if (socketChannel.isBlocking()) {
            socketChannel.configureBlocking(false);
        }

        System.out.println("Registering socket channel with connection handler");

        if (sockets == null) {
            sockets = new HashMap<>();
        }

        sockets.put(socketChannel, socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE));
        numChannels++;
    }

    @Override
    public void shutdown() throws IOException {
        selector.close();
        sockets.clear();
        sockets = null;
        isRunning = false;
    }

    @Override
    public int getChannelCount() {
        return numChannels;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        isRunning = true;
        while (selector.isOpen()) {
            try {
                selector.select(5000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Set<Map.Entry<SocketChannel, SelectionKey>> set = sockets.entrySet();
            for (Iterator<Map.Entry<SocketChannel, SelectionKey>> i = set.iterator(); i.hasNext(); ) {
                Map.Entry<SocketChannel, SelectionKey> en = i.next();

                SelectionKey s = en.getValue();

                if (!s.isValid()) {
                    if (s.attachment() != null) {
                        Client c = (Client) s.attachment();
                        c.disconnect();
                        s.attach(null);
                        s.cancel();
                        i.remove();
                    }
                }
            }

            pollSelections();
        }
        isRunning = false;
    }

    /**
     * Poll selections.
     */
    protected final void pollSelections() {
        Set<SelectionKey> selectionKeySet = selector.selectedKeys();

        SelectionKey currentlySelected;
        for (Iterator<SelectionKey> it = selectionKeySet.iterator(); it.hasNext(); ) {

            currentlySelected = it.next();

            if (currentlySelected.attachment() == null) {
                try {
                    currentlySelected.attach(new Client(currentlySelected));
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            if (currentlySelected.isValid() && currentlySelected.isReadable()) {
                System.out.println("readble selection");
                Client c = (Client) currentlySelected.attachment();
                try {
                    System.out.println("processing read");
                    c.processRead();
                    //Read into read buffer...
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (currentlySelected.isValid() && currentlySelected.isWritable()) {
                System.out.println("writable selection");
                Client c = (Client) currentlySelected.attachment();
                try {
                    System.out.println("processing write");
                    c.processWrite();
                    //Read into read buffer...
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            it.remove();
        }
    }
}