package net;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ISocketHandler extends Runnable {
    /* Tell the socket handler to handle this socket channel */
     void handle(SocketChannel socketChannel) throws Exception;
     /* Shutdown the socket handler */
     void shutdown() throws Exception;
    /* Check if the socket handler is running */
     boolean isRunning();
}
