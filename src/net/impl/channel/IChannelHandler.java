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

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * The interface Channel handler.
 */
public interface IChannelHandler extends Runnable {
    /**
     * Handle.
     *
     * @param socketChannel the socket channel
     * @throws Exception the exception
     */
/* Tell the socket handler to login this socket channel */
    SelectionKey handle(SocketChannel socketChannel) throws Exception;

    /**
     * Shutdown.
     *
     * @throws Exception the exception
     */
/* Shutdown the socket handler */
    void shutdown() throws Exception;

    /**
     * Is running boolean.
     *
     * @return the boolean
     */
/* Check if the socket handler is running */
    boolean isRunning();

    /**
     * Gets channel count.
     *
     * @return the channel count
     */
    int getChannelCount();
}
