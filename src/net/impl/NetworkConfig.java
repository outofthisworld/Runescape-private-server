package net.impl;

import java.math.BigInteger;

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
    PORT = 43594;

    /**
     * Determines if RSA should be decoded in the login block.
     */
    public static final boolean DECODE_RSA = true;

    /**
     * The private RSA modulus value.
     */
    public static final BigInteger RSA_MODULUS = new BigInteger(
            "158524451491299768481335406949632722930001467869157605890403538922079064140031175070843389751771070828946840977662555424706722104406478161144373150173642644879920983864580051507542828646901725343022346327871558268510881848702090892750832077808168455305660457441192814181290396639012661208328185464354643376401");

    /**
     * The private RSA exponent value.
     */
    public static final BigInteger RSA_EXPONENT = new BigInteger(
            "18068841305522212956888101223946113497522177777173311503445602266626952852984312339277051458652820530268900042771858477235137618748438162621854333152221041073570151261267739534657013265105241051273559587745933089288822698802581235318918428986172351596645296040858190395660695176080324899161432348903221384193");


    private NetworkConfig() {
    }
}
