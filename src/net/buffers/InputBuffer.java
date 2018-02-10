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

package net.buffers;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Predicate;

/**
 * The type Input buffer.
 */
public class InputBuffer extends AbstractBuffer {
    private final ByteBuffer inBuffer;

    /**
     * Instantiates a new Input buffer.
     *
     * @param bytes the bytes
     */
    public InputBuffer(byte[] bytes) {
        inBuffer = ByteBuffer.allocate(bytes.length);
        inBuffer.put(bytes);
        inBuffer.flip();
    }


    /**
     * Instantiates a new Input buffer.
     *
     * @param b the b
     */
    public InputBuffer(ByteBuffer b) {
        inBuffer = ByteBuffer.allocate(b.remaining());
        inBuffer.put(b);
        inBuffer.flip();
    }

    /**
     * Instantiates a new Input buffer.
     *
     * @param b the b
     */
    public InputBuffer(ByteBuffer b, int length) {
        if (length > b.remaining()) {
            throw new IllegalArgumentException("");
        }
        inBuffer = ByteBuffer.allocate(length);
        for (int i = 0; i < length; i++) {
            inBuffer.put(b.get());
        }
        inBuffer.flip();
    }

    /**
     * Skip.
     *
     * @param numBytes the num bytes
     */
    @Override
    public void skip(int numBytes) {
        inBuffer.position(inBuffer.position() + numBytes);
    }

    /**
     * Rewind.
     */
    @Override
    public void rewind() {
        inBuffer.rewind();
    }


    @Override
    public byte[] toArray() {
        inBuffer.compact();
        return inBuffer.array();
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(toArray());
    }

    /**
     * Position int.
     *
     * @return the int
     */
    @Override
    public int position() {
        return inBuffer.position();
    }

    private long readBytes(int numBytes) {
        if (numBytes < 1 || numBytes > 8) {
            throw new RuntimeException("Invalid params, numBytes must be between 1-8 inclusive");
        }
        byte[] bytes = new byte[numBytes];
        inBuffer.get(bytes, 0, bytes.length);
        long l = 0L;
        int start;
        int end;
        int increment;
        if (inBuffer.order() == ByteOrder.BIG_ENDIAN) {
            start = 0;
            end = numBytes;
            increment = 1;
        } else {
            start = numBytes - 1;
            end = -1;
            increment = -1;
        }
        for (int i = start; i != end; i += increment) {
            l |= ((bytes[i] & 0xFF) << (inBuffer.order() == ByteOrder.BIG_ENDIAN ? (end - 1 - i) * 8 : i * 8));
        }
        return l;
    }

    private InputBuffer inOrder(ByteOrder b) {
        inBuffer.order(b);
        return this;
    }

    /**
     * Read signed byte byte.
     *
     * @return the byte
     */
    public byte readSignedByte() {
        return inBuffer.get();
    }

    /**
     * Read unsigned byte short.
     *
     * @return the short
     */
    public short readUnsignedByte() {
        return (short) (inBuffer.get() & 0xFF);
    }


    /**
     * Read big unsigned dword long.
     *
     * @return the long
     */
    public long readBigUnsignedDWORD() {
        long bytes = inOrder(ByteOrder.BIG_ENDIAN).readBytes(4);
        return bytes & 0xFFFFFFFFL;
    }

    /**
     * Read little unsigned dword long.
     *
     * @return the long
     */
    public long readLittleUnsignedDWORD() {
        long bytes = inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(4);
        return bytes & 0xFFFFFFFFL;
    }

    /**
     * Read big signed qword long.
     *
     * @return the long
     */
    public long readBigSignedQWORD() {
        return inOrder(ByteOrder.BIG_ENDIAN).readBytes(8);
    }

    /**
     * Read little signed qword long.
     *
     * @return the long
     */
    public long readLittleSignedQWORD() {
        return inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(8);
    }

    /**
     * Read big signed dword int.
     *
     * @return the int
     */
    public int readBigSignedDWORD() {
        return (int) inOrder(ByteOrder.BIG_ENDIAN).readBytes(4);
    }

    /**
     * Read little signed dword int.
     *
     * @return the int
     */
    public int readLittleSignedDWORD() {
        return (int) inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(4);
    }

    /**
     * Read little signed word short.
     *
     * @return the short
     */
    public short readLittleSignedWORD() {
        return (short) inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(2);
    }

    /**
     * Read big signed word short.
     *
     * @return the short
     */
    public short readBigSignedWORD() {
        return (short) inOrder(ByteOrder.BIG_ENDIAN).readBytes(2);
    }

    /**
     * Read little unsigned word int.
     *
     * @return the int
     */
    public int readLittleUnsignedWORD() {
        return (int) (inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(2) & 0xFFFFL);
    }

    /**
     * Read big unsigned word int.
     *
     * @return the int
     */
    public int readBigUnsignedWORD() {
        return (int) (inOrder(ByteOrder.BIG_ENDIAN).readBytes(2) & 0xFFFFL);
    }

    /**
     * Remaining int.
     *
     * @return the int
     */
    @Override
    public int remaining() {
        return inBuffer.remaining();
    }

    @Override
    public int size() {
        return inBuffer.capacity();
    }

    @Override
    public void clear() {
        inBuffer.clear();
    }

    public byte[] readUntil(Predicate<Byte> pred) {
        return readUntil(pred, false);
    }

    public byte[] readUntil(Predicate<Byte> pred, boolean resetPosition) {
        inBuffer.mark();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boolean found = false;
        while (inBuffer.remaining() > 0) {
            byte s = readSignedByte();
            if (pred.test(s)) {
                found = !found;
                break;
            }
            bos.write(s);
        }
        if (resetPosition) {
            inBuffer.reset();
        }
        if (!found) {
            return null;
        }
        return bos.toByteArray();
    }
}