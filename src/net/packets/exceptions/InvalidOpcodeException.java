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

package net.packets.exceptions;

public class InvalidOpcodeException extends Exception {
    private final int opcode;
    private final String message;

    public InvalidOpcodeException(int opcode, String message) {
        this.opcode = opcode;
        this.message = message;
    }

    public int getOpcode() {
        return opcode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
