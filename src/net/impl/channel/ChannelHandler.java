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
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * The type Channel handler.
 */
public class ChannelHandler implements IChannelHandler {
    private static final Logger logger = Logger.getLogger(ChannelHandler.class.getName());
    private static Executor ex = Executors.newCachedThreadPool();
    private final Selector selector;
    private final NetworkEvent networkReadEvent = new NetworkReadEvent();
    private final NetworkEvent networkWriteEvent = new NetworkWriteEvent();
    private final AtomicInteger numChannel = new AtomicInteger();
    private volatile boolean isRunning = false;

    /**
     * Instantiates a new Channel handler.
     *
     * @throws IOException the io exception
     */
    public ChannelHandler() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void handle(final SocketChannel socketChannel) {
        ex.execute(() -> {
            if (!socketChannel.isConnected()) {
                return;
            }

            try {
                if (socketChannel.isBlocking()) {
                    socketChannel.configureBlocking(false);
                }
                socketChannel.register(selector, SelectionKey.OP_READ);
                numChannel.getAndIncrement();
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void shutdown() throws IOException {
        selector.close();
        isRunning = false;
    }

    @Override
    public int getChannelCount() {
        return numChannel.get();
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
                pollSelections();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isRunning = false;
    }

    /**
     * Poll selections.
     */
    protected final void pollSelections() {
        Set<SelectionKey> selectionKeySet = selector.selectedKeys();

        if (selectionKeySet.size() == 0) return;

        SelectionKey currentlySelected;

        for (Iterator<SelectionKey> it = selectionKeySet.iterator(); it.hasNext(); ) {

            currentlySelected = it.next();

            if (currentlySelected.isValid() && currentlySelected.attachment() == null) {
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