package net.impl;

/**
 * The type Network config.
 */
public final class NetworkConfig {
    /**
     * The constant HOST.
     */
    public static final String HOST = "localhost";
    /**
     * The constant MAX_CHANNELS_PER_HANDLER.
     */
    public static final int
            MAX_CHANNELS_PER_HANDLER = 800,
    /**
     * The Default no channel handlers.
     */
    DEFAULT_NO_CHANNEL_HANDLERS = Runtime.getRuntime().availableProcessors() - 1,
    /**
     * The Port.
     */
    PORT = 43595;


    private NetworkConfig() {
    }
}
