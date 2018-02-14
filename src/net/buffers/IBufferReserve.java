package net.buffers;

import java.nio.ByteBuffer;

/**
 * The interface Buffer reserve.
 */
public interface IBufferReserve {
    /**
     * Write value.
     *
     * @param value the value
     */
    void writeValue(long value);

    /**
     * Write byte.
     *
     * @param b the b
     */
    void writeByte(int b);

    /**
     * Write bytes.
     *
     * @param b the b
     */
    void writeBytes(ByteBuffer b);

    /**
     * Write bytes.
     *
     * @param bytes the bytes
     */
    void writeBytes(Byte bytes[]);

    /**
     * Write value.
     *
     * @param value the value
     * @param type  the type
     */
    void writeValue(long value, OutputBuffer.ByteTransformationType type);

    /**
     * Remaining int.
     *
     * @return the int
     */
    int remaining();
}