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
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * The type Input buffer.
 */
public class InputBuffer extends AbstractBuffer {

    private final int UPPER_BOUND;
    private final int RESIZE_THRESHOLD;
    private final int BUFFER_INCREASE_SIZE;
    private final int INITIAL_BUFFER_SIZE;
    private ByteBuffer inBuffer;


    /**
     * Instantiates a new Input buffer.
     */
    public InputBuffer() {
        this(1024, 1024, 512, 8192);
    }

    /**
     * Instantiates a new Input buffer.
     *
     * @param initialSizeBytes the initial size bytes
     * @param increaseSize     the increase size
     * @param resizeThreshold  the widen threshold
     * @param upperBound       the upper bound
     */
    public InputBuffer(int initialSizeBytes, int increaseSize, int resizeThreshold, int upperBound) {
        UPPER_BOUND = upperBound;
        INITIAL_BUFFER_SIZE = initialSizeBytes;
        BUFFER_INCREASE_SIZE = increaseSize;
        RESIZE_THRESHOLD = resizeThreshold;
        inBuffer = ByteBuffer.allocate(initialSizeBytes);
        inBuffer.flip();
    }

    /**
     * Widen.
     *
     * @param size the size
     */
    private void widen(int newSz) {
        ByteBuffer b = ByteBuffer.allocate(inBuffer.capacity() + newSz);
        b.put(inBuffer);
        inBuffer.clear();
        inBuffer = b;
        inBuffer.flip();
    }

    private boolean bufferNeedsResizing(int resizeThreshold, int increaseSize) {
        return inBuffer.remaining() <= resizeThreshold && (UPPER_BOUND == -1 || inBuffer.capacity() + increaseSize <= UPPER_BOUND);
    }

    /**
     * Pipe from int.
     *
     * @param socketChannel the socket channel
     * @return the int
     * @throws IOException             the io exception
     * @throws BufferOverflowException the buffer overflow exception
     */
    public int readFrom(SocketChannel socketChannel) throws IOException, BufferOverflowException {
        Objects.requireNonNull(socketChannel);
        if (bufferNeedsResizing(RESIZE_THRESHOLD, BUFFER_INCREASE_SIZE)) {
            widen(inBuffer.capacity() + BUFFER_INCREASE_SIZE);
        }

        inBuffer.compact();
        if (inBuffer.remaining() == 0) {
            throw new BufferOverflowException();
        }
        int bytesRead = socketChannel.read(inBuffer);
        inBuffer.flip();
        return bytesRead;
    }

    /**
     * Takes data from the given @class ByteBuffer and transfers it into this @class InputBuffer.
     * <p>
     * This @class InputBuffer will attempt to widen within its constraints, and if needed widen more
     * than BUFFER_INCREASE_SIZE to fit the reamining bytes in the source buffer. However, if this
     *
     * @param b the b
     * @return the int
     * @throws java.nio.BufferOverflowException - If there is insufficient space in this buffer for the remaining bytes in the source buffer and this InputBuffer is                                          bounded.
     * @throws IllegalArgumentException         - If the source buffer is this buffer
     * @throws java.nio.ReadOnlyBufferException - If this buffer is read-only
     * @class InputBuffer is bounded, and the source ByteBuffers remaining bytes will exceed the upper bound then BufferOverflowException will be thrown.
     */
    public int readFrom(ByteBuffer b) {
        Objects.requireNonNull(b);

        int resizeAmount = b.remaining() > BUFFER_INCREASE_SIZE ? b.remaining() : BUFFER_INCREASE_SIZE;
        if (bufferNeedsResizing(b.remaining(), resizeAmount)) {
            widen(inBuffer.capacity() + resizeAmount);
        }

        int read = b.remaining();
        inBuffer.compact();
        inBuffer.put(b);
        inBuffer.flip();
        return read;
    }

    /**
     * Pipes the reamining bytes in this @class InputBuffer to the specified @class OutputBuffer
     *
     * @param out the out
     */
    public void pipeTo(OutputBuffer out) {
        Objects.requireNonNull(out);
        while (inBuffer.remaining() != 0) {
            out.writeByte(inBuffer.get());
        }
    }

    /**
     * Pipes the reamining bytes in this @class InputBuffer to the specified @class OutputBuffer
     *
     * @param out the out
     */
    public void pipeTo(ByteBuffer out) {
        Objects.requireNonNull(out);
        if (inBuffer.remaining() > out.remaining()) {
            throw new BufferOverflowException();
        }
        while (inBuffer.remaining() != 0) {
            out.put(inBuffer.get());
        }
    }

    /**
     * Pipe to.
     *
     * @param bytes the bytes
     */
    public void pipeTo(byte[] bytes) {
        Objects.requireNonNull(bytes);
        inBuffer.get(bytes);
    }

    /**
     * Pipe to.
     *
     * @param bytes  the bytes
     * @param offset the offset
     * @param len    the len
     */
    public void pipeTo(byte[] bytes, int offset, int len) {
        Objects.requireNonNull(bytes);
        inBuffer.get(bytes, offset, len);
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
        return toByteBuffer().array();
    }


    @Override
    public ByteBuffer toByteBuffer() {
        return inBuffer.duplicate();
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

    /**
     * Read until byte [ ].
     *
     * @param pred the pred
     * @return the byte [ ]
     */
    public byte[] readUntil(Predicate<Byte> pred) {
        return readUntil(pred, false);
    }

    /**
     * Sets in buffer.
     *
     * @param b the b
     */
    public void setInBuffer(ByteBuffer b) {
        inBuffer = b;
    }

    /**
     * Read until byte [ ].
     *
     * @param pred          the pred
     * @param resetPosition the reset position
     * @return the byte [ ]
     */
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