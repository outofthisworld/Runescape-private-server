package net.impl.decoder;

import net.buffers.InputBuffer;
import net.buffers.OutputBuffer;
import net.impl.session.Client;

import java.util.logging.Logger;

public class LoginRequestDecoder implements ProtocolDecoder {
    private static final Logger logger = Logger.getLogger(LoginRequestDecoder.class.getName());

    @Override
    public void decode(Client c) {
        InputBuffer in = c.getInputBuffer();

        if (in.remaining() < 1) {
            return;
        }

        int op = in.readUnsignedByte();

        if (op != LoginProtocolConstants.LOGIN_REQUEST) {
            return;
        }

        if(in.remaining() > 0)
            in.readUnsignedByte();

        c.write(OutputBuffer.create(73, 10)
                .writeBigQWORD(0)
                .writeByte(0)// login response - 0 means exchange session key to establish encryption
                .writeBigQWORD(c.getServerSessionKey()));// send the net.Reactor part of the session Id used (Client+net.Reactor part together are used as cryption key

        c.setProtocolDecoder(new LoginSessionDecoder());
    }
}
