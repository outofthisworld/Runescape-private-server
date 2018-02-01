package net.buffers;

import java.nio.ByteBuffer;

/**
 * The type Abstract buffer.
 */
public abstract class AbstractBuffer {
    /**
     * To array byte [ ].
     * <p>
     * Returns a byte array which backs this AbstractBuffer.
     * <p>
     * Changes to the byte[] will affect the contents of this AbstractBuffer.
     *
     * @return the byte [ ]
     */
    public abstract byte[] toArray();

    /**
     * This method should be used when another library only accepts a ByteBuffer as a parameter
     * and you don't intend on writing anymore data.
     *
     * @return the byte buffer
     */
    public abstract ByteBuffer toByteBuffer();

    /**
     * Position int.
     *
     * @return the int
     */
    public abstract int position();

    /**
     * Rewind.
     */
    public abstract void rewind();

    /**
     * Skip.
     *
     * @param numBytes the num bytes
     */
    public abstract void skip(int numBytes);

    /**
     * Remaining int.
     *
     * @return the int
     */
    public abstract int remaining();

    /**
     * Size int.
     *
     * @return the int
     */
    public abstract int size();

    public abstract void clear();
}
