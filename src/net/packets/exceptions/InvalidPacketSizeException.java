package net.packets.exceptions;

public class InvalidPacketSizeException extends Exception {
    private final int opcode;
    private final String message;

    public InvalidPacketSizeException(int opcode, String message) {
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
