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
import world.task.DefaultThreadFactory;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


/**
 * The type Channel manager.
 */
public class ChannelManager {
    private static final Logger logger = Logger.getLogger(ChannelManager.class.getName());
    private static ExecutorService executor = Executors.newCachedThreadPool(new DefaultThreadFactory(10));
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


    private IChannelHandler getChannelHandler(List<IChannelHandler> availableHandlers) {
        int min = Integer.MAX_VALUE;
        IChannelHandler minHandler = null;
        for (Iterator<IChannelHandler> it = availableHandlers.iterator(); it.hasNext(); ) {
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
        return min >= maxChannelsPerHandler ? null : minHandler;
    }

    /**
     * Register.
     *
     * @param s the s
     * @throws Exception the exception
     */
    public void register(SocketChannel s) throws Exception {
        IChannelHandler readChannel = getChannelHandler(channelHandlers);

        if (readChannel == null) {
            readChannel = new ChannelHandler();
            addChannelHandler(readChannel, channelHandlers);
        }

        readChannel.handle(s);
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
    public <T extends IChannelHandler> void addChannelHandler(T t, List<T> list) {
        if (!t.isRunning()) {
            if (ChannelManager.executor == null || ChannelManager.executor.isShutdown()) {
                ChannelManager.executor = Executors.newCachedThreadPool();
            }
            ChannelManager.executor.execute(t);
        }
        list.add(t);
    }
}
