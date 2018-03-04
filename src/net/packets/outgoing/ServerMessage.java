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

package net.packets.outgoing;

import net.buffers.OutputBuffer;
import net.impl.session.Client;

public class ServerMessage extends OutgoingPacket {

    private String message;

    public ServerMessage(String message) {
        super();
        this.message = message == null? "":message;
    }

    @Override
    public OutputBuffer encode(Client c) {
        return   writeOpcode(c,OutputBuffer.create(message.getBytes().length + 3), Opcodes.SEND_MESSAGE)
                .createByteReserve(1)
                .toBuffer()
                .writeBytes(message.getBytes())
                .writeByte(10)
                .toLastReserve()
                .writeBytesSinceReserve()
                .toBuffer();
    }
}
