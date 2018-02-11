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

import net.impl.NetworkConfig;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The type Channel manager.
 */
public class ChannelManager {
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private final ArrayList<IChannelHandler> channelHandlers = new ArrayList();
    private final int maxChannelsPerHandler;

    private ChannelManager(int maxChannelsPerHandler) {
        this.maxChannelsPerHandler = maxChannelsPerHandler;
    }

    /**
     * Create channel manager.
     *
     * @return the channel manager
     */
    public static ChannelManager create() {
        return new ChannelManager(NetworkConfig.MAX_CHANNELS_PER_HANDLER);
    }

    /**
     * Create channel manager.
     *
     * @param numHandlers the num handlers
     * @return the channel manager
     * @throws IOException the io exception
     */
    public static ChannelManager create(int maxChannelsPerHandler, int numHandlers) throws IOException {
        ChannelManager s = new ChannelManager(maxChannelsPerHandler);
        for (int i = 0; i < numHandlers; i++) {
            s.addChannelHandler(new ChannelHandler());
        }
        return s;
    }

    /**
     * Register.
     *
     * @param s the s
     * @throws Exception the exception
     */
    public SelectionKey register(SocketChannel s) throws Exception {
        int min = Integer.MAX_VALUE;
        IChannelHandler minHandler = null;
        for (Iterator<IChannelHandler> it = channelHandlers.iterator(); it.hasNext(); ) {
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

        if (minHandler == null || min >= maxChannelsPerHandler) {
            ChannelHandler socketHandler = new ChannelHandler();
            addChannelHandler(socketHandler);
            return socketHandler.handle(s);
        } else {
            return minHandler.handle(s);
        }
    }

    /**
     * Shutdown handlers.
     *
     * @throws Exception the exception
     */
    public void shutdownHandlers() throws Exception {
        for (int i = 0; i < channelHandlers.size(); i++) {
            if (channelHandlers.get(i) != null && channelHandlers.get(i).isRunning()) {
                channelHandlers.get(i).shutdown();
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
    public <T extends IChannelHandler> void addChannelHandler(T t) {
        if (!t.isRunning()) {
            if (ChannelManager.executor == null || ChannelManager.executor.isShutdown()) {
                ChannelManager.executor = Executors.newCachedThreadPool();
            }
            ChannelManager.executor.execute(t);
        }
        channelHandlers.add(t);
    }
}
