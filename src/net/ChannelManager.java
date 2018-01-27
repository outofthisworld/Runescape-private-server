package net;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChannelManager {
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static int MAX_HANDLER_CHANNELS = 1000;
    private ArrayList<IChannelHandler> socketHandlers = new ArrayList();

    private ChannelManager() {

    }

    public static ChannelManager create() {
        return new ChannelManager();
    }

    public static ChannelManager create(int numHandlers) throws IOException {
        ChannelManager s = new ChannelManager();
        for (int i = 0; i < numHandlers; i++) {
            s.addSocketHandler(new ChannelHandler());
        }
        return s;
    }

    public static ChannelManager create(int numHandlers, int maxHandlerChannels) throws IOException {
        MAX_HANDLER_CHANNELS = maxHandlerChannels;
        return ChannelManager.create(numHandlers);
    }

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

        if (minHandler == null || min >= MAX_HANDLER_CHANNELS) {
            ChannelHandler socketHandler = new ChannelHandler();
            addSocketHandler(socketHandler);
            socketHandler.handle(s);
        } else {
            minHandler.handle(s);
        }
    }

    public void shutdownHandlers() throws Exception {
        for (int i = 0; i < socketHandlers.size(); i++) {
            if (socketHandlers.get(i) != null && socketHandlers.get(i).isRunning()) {
                socketHandlers.get(i).shutdown();
            }
        }
    }

    public void shutdown() throws Exception {
        shutdownHandlers();
        executor.shutdownNow();
        executor = null;
    }

    public <T extends IChannelHandler> void addSocketHandler(T t) {
        if (!t.isRunning()) {
            if (executor == null || executor.isShutdown()) {
                executor = Executors.newCachedThreadPool();
            }
            executor.execute(t);
        }
        socketHandlers.add(t);
    }
}
