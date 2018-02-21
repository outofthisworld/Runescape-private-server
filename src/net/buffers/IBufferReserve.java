package net.buffers;

import java.nio.ByteBuffer;

/**
 * The interface Buffer reserve.
 */
public interface IBufferReserve<T> {
    /**
     * Write value.
     *
     * @param value the value
     */
    IBufferReserve<T> writeValue(long value);

    /**
     * Write byte.
     *
     * @param b the b
     */
    IBufferReserve<T> writeByte(int b);

    /**
     * Write bytes.
     *
     * @param b the b
     */
    IBufferReserve<T> writeBytes(ByteBuffer b);

    /**
     * Write bytes.
     *
     * @param bytes the bytes
     */
    IBufferReserve<T> writeBytes(Byte bytes[]);

    /**
     * Write value.
     *
     * @param value the value
     * @param type  the type
     */
    IBufferReserve<T> writeValue(long value,ByteTransformationType type);

    IBufferReserve<T> writeBytesSinceReserve();

    /**
     * Remaining int.
     *
     * @return the int
     */
    int remaining();

    /**
     * Bytes since reserve int.
     *
     * @return the int
     */
    int bytesSinceReserve();

    T toBuffer();
}