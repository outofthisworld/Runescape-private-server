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

import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * <p>
 * An unbounded buffer that can be written to and dynamically resized as it is written to.
 * Built around a ByteBuffer, but makes them easier to work with as the buffer only can be written to.
 * Reading from the @class ByteBuffer is done internally through one of its pipe methods.
 * <p>
 * This class is not thread safe, any attempts to use it from multiple threads will result
 * in inconsistencies unless synchronized correctly from user level code.
 */
public class OutputBuffer extends AbstractBuffer {
    private static final int INITIAL_SIZE = 512;
    private static final int INCREASE_SIZE_BYTES = 512;
    private final int increaseSizeBytes;
    private ByteBuffer out;
    private Order currentByteOrder = Order.BIG_ENDIAN;
    private int bitIndex = 0;

    private OutputBuffer() {
        this(OutputBuffer.INITIAL_SIZE, OutputBuffer.INCREASE_SIZE_BYTES);
    }

    private OutputBuffer(final int initialSize, final int increaseSizeBytes) {
        this.out = ByteBuffer.allocate(initialSize);
        this.increaseSizeBytes = increaseSizeBytes;
    }

    private OutputBuffer(final byte[] bytes, final int increaseSizeBytes) {
        this.out = ByteBuffer.allocate(bytes.length);
        this.out.put(bytes);
        this.increaseSizeBytes = increaseSizeBytes;
    }

    /**
     * Instantiates a new Output buffer.
     *
     * @param bytes the bytes
     */
    public OutputBuffer(final byte[] bytes) {
        this(bytes, 256);
    }

    /**
     * Creates a new OutputBuffer with an initial size of 256 bytes
     * that is dynamically resized every 256 bytes.
     *
     * @return the output buffer
     */
    public static OutputBuffer create() {
        return new OutputBuffer();
    }

    /**
     * Create output buffer.
     *
     * @param initialSize       the initial size
     * @param increaseSizeBytes the amount of bytes that the buffer should widen by if its capacity is reached.
     * @return the output buffer
     */
    public static OutputBuffer create(final int initialSize, final int increaseSizeBytes) {
        return new OutputBuffer(initialSize, increaseSizeBytes);
    }

    /**
     * Creates an output buffer of initial size. Dynamically resized every 256 bytes.
     *
     * @param initialSize the initial size
     * @return the output buffer
     */
    public static OutputBuffer create(final int initialSize) {
        return new OutputBuffer(initialSize, OutputBuffer.INCREASE_SIZE_BYTES);
    }

    /**
     * Wraps the specified byte array, creating an OutputBuffer with equal length as the byte array.
     * Note that the first write operation to this output buffer after wrapping a byte array
     * will cause the internal @class ByteBuffer to widen by @param increaseSizeBytes which may be costly in some
     * situations.
     * The intended use of this method is to turn byte array into an output buffer without subsequent
     * writes to the OutputBuffer unless truly necessary.
     *
     * @param bytes             the bytes
     * @param increaseSizeBytes the widen size bytes
     * @return the output buffer
     */
    public static OutputBuffer wrap(final byte[] bytes, final int increaseSizeBytes) {
        return new OutputBuffer(bytes, increaseSizeBytes);
    }

    /**
     * Wrap output buffer.
     *
     * @param bytes the bytes
     * @return the output buffer
     */
    public static OutputBuffer wrap(final byte[] bytes) {
        return new OutputBuffer(bytes);
    }

    /**
     * Pipe all to int.
     *
     * @param c the c
     * @return the int
     * @throws IOException the io exception
     */
    public int pipeAllTo(final SocketChannel c) throws IOException {
        int bytesWritten = 0;
        final int length = this.out.position();
        System.out.println("writing " + length + " bytes");
        while (bytesWritten != length) {
            bytesWritten += this.pipeTo(c);
        }
        System.out.println("wrote : " + bytesWritten);
        return bytesWritten;
    }

    /**
     * Pipe to int.
     *
     * @param byteArr the byte arr
     * @return the int
     */
    public int pipeTo(final byte[] byteArr) {
        this.out.flip();
        this.out.get(byteArr, 0, byteArr.length);
        this.out.compact();
        return byteArr.length;
    }

    /**
     * Pipe to int.
     *
     * @param c the c
     * @return the int
     * @throws IOException the io exception
     */
    public int pipeTo(final SocketChannel c) throws IOException {
        this.out.flip();
        int bytesWritten;
        try {
            bytesWritten = c.write(this.out);
        } catch (final Exception e) {
            e.printStackTrace();
            bytesWritten = -1;
        }
        this.out.compact();
        return bytesWritten;
    }

    /**
     * Pipe to output buffer.
     *
     * @param b the b
     * @return the output buffer
     * @throws Exception the exception
     */
    public OutputBuffer pipeTo(final ByteBuffer b) throws Exception {
        this.out.flip();
        if (b.capacity() - b.position() < this.out.limit()) {
            throw new Exception("Not enough room in buffer b");
        }
        if (b.limit() != b.capacity()) {
            throw new Exception("Buffer may be in readInBuffer mode");
        }
        b.put(this.out);
        this.out.compact();
        return this;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    @Override
    public int size() {
        return this.out.position();
    }

    /**
     * Clear.
     */
    @Override
    public void clear() {
        this.out.clear();
    }

    /**
     * Widen.
     *
     * @param newSize the new size
     * @return the output buffer
     */
    public OutputBuffer widen(final int newSize) {
        if (newSize == this.out.capacity()) {
            return this;
        }
        if (newSize < this.out.position()) {
            throw new IllegalArgumentException("newSize to small, cannot truncate current buffer");
        }
        final ByteBuffer x = ByteBuffer.allocate(newSize);
        this.out.flip();
        x.put(this.out);
        this.out.clear();
        this.out = x;
        return this;
    }

    /**
     * Truncate.
     *
     * @param newSize the new size
     * @return the output buffer
     */
    public OutputBuffer truncate(final int newSize) {
        if (newSize < 0 || newSize > this.out.capacity()) {
            throw new IllegalArgumentException("Invalid newsize, must be >= 0");
        }
        if (newSize == this.out.capacity()) {
            return this;
        }
        final byte[] bytes = new byte[0];
        final ByteBuffer x = ByteBuffer.allocate(newSize);
        this.out.flip();
        this.out.get(bytes, 0, bytes.length);
        x.put(bytes);
        this.out.clear();
        this.out = x;
        return this;
    }

    /**
     * Write byte output buffer.
     *
     * @param b the b
     * @return the output buffer
     */
    public OutputBuffer writeByte(final int b) {
        return this.writeByte(b, true);
    }

    /**
     * Write byte output buffer.
     *
     * @param b the b
     * @return the output buffer
     */
    private OutputBuffer writeByte(final int b, final boolean resetBitIndex) {
        if (this.out.remaining() < 1) {
            this.widen(this.out.capacity() + this.increaseSizeBytes);
        }
        this.out.put((byte) b);
        if (resetBitIndex) {
            this.bitIndex = 0;
        }
        return this;
    }

    /**
     * Write bit output buffer.
     *
     * @param value the value
     * @return the output buffer
     */
    public OutputBuffer writeBit(final boolean value) {
        this.writeBits(value ? 1 : 0, 1);
        return this;
    }

    /**
     * Write bit output buffer.
     *
     * @param value the value
     * @return the output buffer
     */
    public OutputBuffer writeBit(final int value) {
        this.writeBits(value, 1);
        return this;
    }

    /**
     * Write bits output buffer.
     *
     * @param value  the value
     * @param amount the amount
     * @return the output buffer
     */
    public OutputBuffer writeBits(final long value, int amount) {
        if (this.bitIndex != 0) {
            final int remainingBits = 8 - this.bitIndex;
            final int bytePos = this.out.position() - 1;
            final byte current = this.out.get(bytePos);
            final int shiftAmount = amount - remainingBits;

            if (shiftAmount < 0) {
                this.out.put(bytePos, (byte) (current | (value << remainingBits - amount)));
            } else {
                this.out.put(bytePos, (byte) (current | (value >> shiftAmount)));
            }

            final int bitsWritten = amount < remainingBits ? amount : remainingBits;
            this.bitIndex += bitsWritten;
            amount -= bitsWritten;
            if (this.bitIndex >= 8) {
                this.bitIndex = 0;
            }
        }
        if (amount <= 0) {
            return this;
        }
        this.bitIndex = amount & 7;
        int newAmount = amount - this.bitIndex;
        final long newValue = (value >> this.bitIndex);
        for (; newAmount >= 8; newAmount -= 8) {
            this.writeByte((byte) (newValue >> newAmount), false);
        }
        this.writeByte((byte) (value << (8 - this.bitIndex)), false);
        return this;
    }


    /**
     * Create byte reserve buffer reserve.
     *
     * @param numBytes the num bytes
     * @return the buffer reserve
     */
    public IBufferReserve createByteReserve(final int numBytes) {
        return new OutputBufferReserve(numBytes);
    }

    /**
     * Get bit position int.
     *
     * @return the int
     */
    public int getBitPosition() {
        return (this.out.position() * 8) + this.bitIndex;
    }

    /**
     * Write byte output buffer.
     *
     * @param b    the b
     * @param type the type
     * @return the output buffer
     */
    public OutputBuffer writeByte(final int b, final ByteTransformationType type) {
        return this.writeByte(this.transformValue(b, type));
    }

    /**
     * Write bytes output buffer.
     *
     * @param bytes the bytes
     * @return the output buffer
     */
    public OutputBuffer writeBytes(final byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }
        for (int i = 0; i < bytes.length; i++) {
            this.writeByte(bytes[i]);
        }
        return this;
    }

    /**
     * Writes the bytes in reverse.
     *
     * @param bytes the bytes
     * @return the output buffer
     */
    public OutputBuffer writeBytesReverse(final byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }
        for (int i = bytes.length - 1; i >= 0; i--) {
            this.writeByte(bytes[i]);
        }
        return this;
    }

    /**
     * Write bytes output buffer.
     *
     * @param val    the val
     * @param repeat the repeat
     * @return the output buffer
     */
    public OutputBuffer writeBytes(final int val, final int repeat) {
        if (repeat <= 0) {
            throw new IllegalArgumentException("repeat cannot be > 0");
        }
        for (int i = 0; i < repeat; i++) {
            this.writeByte((byte) val);
            System.out.println("writing byte");
        }
        return this;
    }

    /**
     * Transform a value.
     */
    private byte transformValue(final long value, final ByteTransformationType type) {
        switch (type) {
            case A:
                return (byte) (value + 128);
            case S:
                return (byte) (128 - value);
            case C:
                return (byte) (-value);
            default:
                return (byte) value;
        }
    }

    /**
     * Long to byte array byte [ ].
     *
     * @param value    the value
     * @param numBytes the num bytes
     * @param type     the type
     * @return the byte [ ]
     */
    private byte[] longToByteArray(final long value, final int numBytes, final ByteTransformationType type) {
        if (numBytes < 1 || numBytes > 8) {
            throw new RuntimeException("Invalid params, numBytes must be between 1-8 inclusive");
        }

        int start = 0;
        int end = 0;
        int increment = 0;

        switch (this.currentByteOrder) {
            case BIG_ENDIAN:
                start = numBytes - 1;
                end = -1;
                increment = -1;
                break;
            case LITTLE_ENDIAN:
                start = 0;
                end = numBytes;
                increment = 1;
                break;
            case BIG_MIDDLE_ENDIAN:
                start = numBytes / 2;
                start = start % 2 == 0 ? start - 1 : start;
                end = -1;
                increment = -1;
                break;
            case LITTLE_MIDDLE_ENDIAN:
                start = numBytes / 2;
                end = numBytes;
                increment = 1;
                break;
        }

        int bytesWritten = 0;
        final byte[] bytes = new byte[numBytes];
        for (int i = start; i != end; i += increment) {
            if (i == 0) {
                this.transformValue(value, type);
            }

            bytes[bytesWritten++] = (byte) (value >> (i * 8));

            if (bytesWritten == numBytes) {
                break;
            }

            if (i == end + 1 && this.currentByteOrder == Order.BIG_MIDDLE_ENDIAN) {
                i = numBytes;
            }

            if (i == end - 1 && this.currentByteOrder == Order.LITTLE_MIDDLE_ENDIAN) {
                i = -1;
            }
        }
        return bytes;
    }

    /**
     * Write bytes output buffer.
     *
     * @param value    the value
     * @param numBytes the num bytes
     * @param type     the type
     * @return the output buffer
     */
    public OutputBuffer writeBytes(final long value, final int numBytes, final ByteTransformationType type) {
        final byte[] longBytes = this.longToByteArray(value, numBytes, type);
        for (final byte b : longBytes) {
            this.writeByte(b);
        }
        return this;
    }

    /**
     * Out order output buffer.
     *
     * @param order the order
     * @return the output buffer
     */
    public OutputBuffer order(final Order order) {
        this.currentByteOrder = order;
        return this;
    }

    /**
     * Write little dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleDWORD(final long x) {
        return this.order(Order.LITTLE_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write big dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigDWORD(final long x) {
        return this.order(Order.BIG_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write big qword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigQWORD(final long x) {
        return this.order(Order.BIG_ENDIAN).writeBytes(x, 8, ByteTransformationType.NONE);
    }

    /**
     * Write little qword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleQWORD(final long x) {
        return this.order(Order.LITTLE_ENDIAN).writeBytes(x, 8, ByteTransformationType.NONE);
    }

    /**
     * Write big word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigWORD(final int x) {
        return this.order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * Write big word output buffer.
     * <p>
     * Transforms the LSB with +128 using ByteTransformationType.A
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigWORDA(final int x) {
        return this.order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.A);
    }

    /**
     * Write little word output buffer.
     * <p>
     * Transforms the LSB with +128 using ByteTransformationType.A
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleWORDA(final int x) {
        return this.order(Order.LITTLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.A);
    }

    /**
     * Write little word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleWORD(final int x) {
        return this.order(Order.LITTLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * Write big middle dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigMiddleDWORD(final long x) {
        return this.order(Order.BIG_MIDDLE_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write little middle dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleMiddleDWORD(final long x) {
        return this.order(Order.LITTLE_MIDDLE_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write little middle word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleMiddleWORD(final int x) {
        return this.order(Order.LITTLE_MIDDLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * Write big middle word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigMiddleWORD(final int x) {
        return this.order(Order.BIG_MIDDLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    @Override
    public byte[] toArray() {
        final ByteBuffer dup = this.toByteBuffer();
        dup.flip();
        final byte[] bytes = new byte[dup.remaining()];
        dup.get(bytes);
        return bytes;
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return this.out.duplicate();
    }

    @Override
    public int position() {
        return this.out.position();
    }

    @Override
    public void rewind() {
        this.out.rewind();
    }

    @Override
    public void skip(final int numBytes) {
        this.out.position(this.out.position() + numBytes);
    }

    @Override
    public int remaining() {
        return this.out.remaining();
    }

    /**
     * The enum Order.
     */
    public enum Order {
        /**
         * Big endian order.
         */
        BIG_ENDIAN,
        /**
         * Little endian order.
         */
        LITTLE_ENDIAN,
        /**
         * Big middle endian order.
         */
        BIG_MIDDLE_ENDIAN,
        /**
         * Little middle endian order.
         */
        LITTLE_MIDDLE_ENDIAN
    }

    /**
     * The enum Byte transformation type.
     */
    public enum ByteTransformationType {
        /**
         * A byte transformation type.
         */
        A, /**
         * C byte transformation type.
         */
        C, /**
         * S byte transformation type.
         */
        S, /**
         * None byte transformation type.
         */
        NONE
    }

    private class OutputBufferReserve implements IBufferReserve {
        private final int reserveIndex;
        private final int numBytes;
        private int cursor = 0;

        /**
         * Instantiates a new Output buffer reserve.
         *
         * @param numBytes the num bytes
         */
        public OutputBufferReserve(final int numBytes) {
            this.reserveIndex = OutputBuffer.this.out.position();
            System.out.println("index=" + this.reserveIndex);
            this.numBytes = numBytes;
            for (int i = 0; i < numBytes; i++) {
                OutputBuffer.this.writeByte(0);
            }
        }

        @Override
        public void writeValue(final long value) {
            this.writeValue(value, ByteTransformationType.NONE);
        }

        @Override
        public void writeByte(final int b) {
            if (this.remaining() == 0) {
                throw new InvalidStateException("Cannot exceed reserved bytes (" + this.numBytes + "). To many bytes written. In class " + this.getClass().getName());
            }
            OutputBuffer.this.out.put(this.reserveIndex + this.cursor++, (byte) b);
        }

        @Override
        public void writeBytes(final ByteBuffer b) {
            Objects.requireNonNull(b);
            while (b.remaining() != 0 && this.remaining() != 0) {
                this.writeByte(b.get());
            }
        }

        @Override
        public void writeBytes(final Byte[] bytes) {
            Objects.requireNonNull(bytes);
            for (int i = 0; i < bytes.length && this.remaining() != 0; i++) {
                this.writeByte(bytes[i]);
            }
        }

        @Override
        public void writeValue(final long value, final ByteTransformationType type) {
            final byte[] bytes = OutputBuffer.this.longToByteArray(value, this.numBytes, type);
            for (final byte b : bytes) {
                this.writeByte(b);
            }
        }

        @Override
        public int bytesSinceReserve() {
            return ((OutputBuffer.this.out.position() - 1) - this.reserveIndex - (this.numBytes - 1));
        }

        @Override
        public int remaining() {
            return this.numBytes - this.cursor;
        }
    }
}