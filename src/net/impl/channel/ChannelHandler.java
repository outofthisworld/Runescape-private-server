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

package net.impl.channel;

import net.impl.events.NetworkEvent;
import net.impl.events.NetworkReadEvent;
import net.impl.events.NetworkWriteEvent;
import net.impl.session.Client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The type Channel handler.
 */
public class ChannelHandler implements IChannelHandler {
    private static final Logger logger = Logger.getLogger(ChannelHandler.class.getName());
    private final Selector selector;
    private final NetworkEvent networkReadEvent = new NetworkReadEvent();
    private final NetworkEvent networkWriteEvent = new NetworkWriteEvent();
    private volatile boolean isRunning = false;
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

        socketChannel.register(selector, SelectionKey.OP_READ);
        numChannels++;
    }

    @Override
    public void shutdown() throws IOException {
        selector.close();
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
                selector.select(500);
            } catch (IOException e) {
                e.printStackTrace();
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
                }
            }

            if (currentlySelected.isValid() && currentlySelected.isReadable()) {
                Client c = (Client) currentlySelected.attachment();
                c.execute(networkReadEvent);
            }

            if (currentlySelected.isValid() && currentlySelected.isWritable()) {
                Client c = (Client) currentlySelected.attachment();
                c.execute(networkWriteEvent);
            }

            it.remove();
        }
    }
}