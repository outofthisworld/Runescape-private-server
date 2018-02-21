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

import util.Preconditions;

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
    private Order currentByteOrder = Order.BIG_ENDIAN;
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
        Preconditions.greaterThanOrEqualTo(initialSizeBytes, 0);
        UPPER_BOUND = upperBound;
        INITIAL_BUFFER_SIZE = initialSizeBytes;
        BUFFER_INCREASE_SIZE = increaseSize;
        RESIZE_THRESHOLD = resizeThreshold;
        inBuffer = ByteBuffer.allocate(initialSizeBytes);
        inBuffer.flip();
    }

    public InputBuffer(InputBuffer in, int size) {
        this(size, 1024, 512, -1);
        inBuffer.compact();
        int cSize = 0;
        while (in.remaining() != 0) {
            if (cSize == size) {
                break;
            }
            inBuffer.put(in.readSignedByte());
            cSize++;
        }
        inBuffer.flip();
    }


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
        inBuffer.compact();

        if (bufferNeedsResizing(RESIZE_THRESHOLD, BUFFER_INCREASE_SIZE)) {
            System.out.println("reszing buf to " + inBuffer.capacity() + BUFFER_INCREASE_SIZE);
            widen(inBuffer.capacity() + BUFFER_INCREASE_SIZE);
        }

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

        inBuffer.compact();
        if (bufferNeedsResizing(b.remaining(), resizeAmount)) {
            widen(inBuffer.capacity() + resizeAmount);
        }

        int read = b.remaining();
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

    private long readBytes(int numBytes, ByteTransformationType type) {
        Preconditions.inRangeClosed(numBytes,1,8);
        Preconditions.notNull(type);

        long l = 0L;
        int start;
        int end;
        int increment;

        switch(currentByteOrder){
            case BIG_ENDIAN:
                start = (numBytes-1) * 8;
                end = -8;
                increment = -8;
                break;
            case LITTLE_ENDIAN:
                start = 0;
                end = 64;
                increment = 8;
                break;
            default:
                throw new IllegalStateException("");
        }

        for (int i = start; i != end; i += increment) {
            byte value = inBuffer.get();

            if(i == 0){
                value = type.transformValue(value);
            }

            long val = value & 0xFF;
            l |= (val << i);
        }

        return l;
    }

    private InputBuffer inOrder(Order order) {
        currentByteOrder = order;
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
    public int readUnsignedByte() {
        return (inBuffer.get() & 0xFF);
    }


    /**
     * Read big unsigned dword long.
     *
     * @return the long
     */
    public long readBigUnsignedDWORD() {
        long bytes = inOrder(Order.BIG_ENDIAN).readBytes(4,ByteTransformationType.NONE);
        return bytes & 0xFFFFFFFFL;
    }

    /**
     * Read little unsigned dword long.
     *
     * @return the long
     */
    public long readLittleUnsignedDWORD() {
        long bytes = inOrder(Order.LITTLE_ENDIAN).readBytes(4,ByteTransformationType.NONE);
        return bytes & 0xFFFFFFFFL;
    }

    /**
     * Read big signed qword long.
     *
     * @return the long
     */
    public long readBigSignedQWORD() {
        return inOrder(Order.BIG_ENDIAN).readBytes(8,ByteTransformationType.NONE);
    }

    /**
     * Read little signed qword long.
     *
     * @return the long
     */
    public long readLittleSignedQWORD() {
        return inOrder(Order.LITTLE_ENDIAN).readBytes(8,ByteTransformationType.NONE);
    }

    /**
     * Read big signed dword int.
     *
     * @return the int
     */
    public int readBigSignedDWORD() {
        return (int) inOrder(Order.BIG_ENDIAN).readBytes(4,ByteTransformationType.NONE);
    }

    /**
     * Read little signed dword int.
     *
     * @return the int
     */
    public int readLittleSignedDWORD() {
        return (int) inOrder(Order.LITTLE_ENDIAN).readBytes(4,ByteTransformationType.NONE);
    }

    /**
     * Read little signed word short.
     *
     * @return the short
     */
    public short readLittleSignedWORD() {
        return (short) inOrder(Order.LITTLE_ENDIAN).readBytes(2,ByteTransformationType.NONE);
    }

    /**
     * Read big signed word short.
     *
     * @return the short
     */
    public short readBigSignedWORD() {
        return (short) inOrder(Order.BIG_ENDIAN).readBytes(2,ByteTransformationType.NONE);
    }

    /**
     * Read little unsigned word int.
     *
     * @return the int
     */
    public int readLittleUnsignedWORD() {
        return (int) (inOrder(Order.LITTLE_ENDIAN).readBytes(2,ByteTransformationType.NONE) & 0xFFFFL);
    }

    /**
     * Read big unsigned word int.
     *
     * @return the int
     */
    public int readBigUnsignedWORD() {
        return (int) (inOrder(Order.BIG_ENDIAN).readBytes(2,ByteTransformationType.NONE) & 0xFFFFL);
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
        inBuffer.position(inBuffer.capacity());
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
                found = true;
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