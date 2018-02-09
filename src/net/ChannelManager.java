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
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The type Channel manager.
 */
public class ChannelManager {
    private static int MAX_HANDLER_CHANNELS = 300;
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private final ArrayList<IChannelHandler> socketHandlers = new ArrayList();

    private ChannelManager() {

    }

    /**
     * Create channel manager.
     *
     * @return the channel manager
     */
    public static ChannelManager create() {
        return new ChannelManager();
    }

    /**
     * Create channel manager.
     *
     * @param numHandlers the num handlers
     * @return the channel manager
     * @throws IOException the io exception
     */
    public static ChannelManager create(int numHandlers) throws IOException {
        ChannelManager s = new ChannelManager();
        for (int i = 0; i < numHandlers; i++) {
            s.addSocketHandler(new ChannelHandler());
        }
        return s;
    }

    /**
     * Create channel manager.
     *
     * @param numHandlers        the num handlers
     * @param maxHandlerChannels the max handler channels
     * @return the channel manager
     * @throws IOException the io exception
     */
    public static ChannelManager create(int numHandlers, int maxHandlerChannels) throws IOException {
        ChannelManager.MAX_HANDLER_CHANNELS = maxHandlerChannels;
        return ChannelManager.create(numHandlers);
    }

    /**
     * Register.
     *
     * @param s the s
     * @throws Exception the exception
     */
    public void register(SocketChannel s) throws Exception {
        int min = Integer.MAX_VALUE;
        IChannelHandler minHandler = null;
        for (Iterator<IChannelHandler> it = socketHandlers.iterator(); it.hasNext(); ) {
            IChannelHandler handler = it.next();
            if (!handler.isRunning()) {
                it.remove();
                continue;
            }
            if (handler.getChannelCount() < min) {
                min = handler.getChannelCount();
                minHandler = handler;
            }
        }

        if (minHandler == null || min >= ChannelManager.MAX_HANDLER_CHANNELS) {
            ChannelHandler socketHandler = new ChannelHandler();
            addSocketHandler(socketHandler);
            socketHandler.handle(s);
        } else {
            minHandler.handle(s);
        }
    }

    /**
     * Shutdown handlers.
     *
     * @throws Exception the exception
     */
    public void shutdownHandlers() throws Exception {
        for (int i = 0; i < socketHandlers.size(); i++) {
            if (socketHandlers.get(i) != null && socketHandlers.get(i).isRunning()) {
                socketHandlers.get(i).shutdown();
            }
        }
    }

    /**
     * Shutdown.
     *
     * @throws Exception the exception
     */
    public void shutdown() throws Exception {
        shutdownHandlers();
        ChannelManager.executor.shutdownNow();
        ChannelManager.executor = null;
    }

    /**
     * Add socket handler.
     *
     * @param <T> the type parameter
     * @param t   the t
     */
    public <T extends IChannelHandler> void addSocketHandler(T t) {
        if (!t.isRunning()) {
            if (ChannelManager.executor == null || ChannelManager.executor.isShutdown()) {
                ChannelManager.executor = Executors.newCachedThreadPool();
            }
            ChannelManager.executor.execute(t);
        }
        socketHandlers.add(t);
    }
}
