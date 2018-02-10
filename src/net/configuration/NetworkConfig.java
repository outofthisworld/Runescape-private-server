package net.configuration;

/**
 * The type Network config.
 */
public final class NetworkConfig {
    /**
     * The constant HOST.
     */
    public static final String HOST = "localhost";
    /**
     * The constant PORT.
     */
    public static final int PORT = 43595;
    /**
     * The constant DEFAULT_NO_CHANNEL_HANDLERS.
     */
    public static final int DEFAULT_NO_CHANNEL_HANDLERS = Runtime.getRuntime().availableProcessors() - 1;
    /**
     * The constant MAX_CHANNELS_PER_HANDLER.
     */
    public static final int MAX_CHANNELS_PER_HANDLER = 300;


    private NetworkConfig() {
    }
}
