package net.impl.decoder;

public class Decoders {

    public static final ProtocolDecoder LOGIN_REQUEST_DECODER = new LoginRequestDecoder();
    public static final ProtocolDecoder LOGIN_SESSION_DECODER = new LoginSessionDecoder();
    public static final ProtocolDecoder GAME_PACKET_DECODER = new GamePacketDecoder();
}
