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

    private OutputBuffer(int initialSize, int increaseSizeBytes) {
        out = ByteBuffer.allocate(initialSize);
        this.increaseSizeBytes = increaseSizeBytes;
    }

    private OutputBuffer(byte[] bytes, int increaseSizeBytes) {
        out = ByteBuffer.allocate(bytes.length);
        out.put(bytes);
        this.increaseSizeBytes = increaseSizeBytes;
    }

    /**
     * Instantiates a new Output buffer.
     *
     * @param bytes the bytes
     */
    public OutputBuffer(byte[] bytes) {
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
    public static OutputBuffer create(int initialSize, int increaseSizeBytes) {
        return new OutputBuffer(initialSize, increaseSizeBytes);
    }

    /**
     * Creates an output buffer of initial size. Dynamically resized every 256 bytes.
     *
     * @param initialSize the initial size
     * @return the output buffer
     */
    public static OutputBuffer create(int initialSize) {
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
    public static OutputBuffer wrap(byte[] bytes, int increaseSizeBytes) {
        return new OutputBuffer(bytes, increaseSizeBytes);
    }

    /**
     * Wrap output buffer.
     *
     * @param bytes the bytes
     * @return the output buffer
     */
    public static OutputBuffer wrap(byte[] bytes) {
        return new OutputBuffer(bytes);
    }

    /**
     * Pipe all to int.
     *
     * @param c the c
     * @return the int
     * @throws IOException the io exception
     */
    public int pipeAllTo(SocketChannel c) throws IOException {
        int bytesWritten = 0;
        int length = out.position();
        System.out.println("writing " + length + " bytes");
        while (bytesWritten != length) {
            bytesWritten += pipeTo(c);
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
    public int pipeTo(byte[] byteArr) {
        out.flip();
        out.get(byteArr, 0, byteArr.length);
        out.compact();
        return byteArr.length;
    }

    /**
     * Pipe to int.
     *
     * @param c the c
     * @return the int
     * @throws IOException the io exception
     */
    public int pipeTo(SocketChannel c) throws IOException {
        out.flip();
        int bytesWritten;
        try {
            bytesWritten = c.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            bytesWritten = -1;
        }
        out.compact();
        return bytesWritten;
    }

    /**
     * Pipe to output buffer.
     *
     * @param b the b
     * @return the output buffer
     * @throws Exception the exception
     */
    public OutputBuffer pipeTo(ByteBuffer b) throws Exception {
        out.flip();
        if (b.capacity() - b.position() < out.limit()) {
            throw new Exception("Not enough room in buffer b");
        }
        if (b.limit() != b.capacity()) {
            throw new Exception("Buffer may be in readInBuffer mode");
        }
        b.put(out);
        out.compact();
        return this;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    @Override
    public int size() {
        return out.position();
    }

    /**
     * Clear.
     */
    @Override
    public void clear() {
        out.clear();
    }

    /**
     * Widen.
     *
     * @param newSize the new size
     * @return the output buffer
     */
    public OutputBuffer widen(int newSize) {
        if (newSize == out.capacity()) {
            return this;
        }
        if (newSize < out.position()) {
            throw new IllegalArgumentException("newSize to small, cannot truncate current buffer");
        }
        ByteBuffer x = ByteBuffer.allocate(newSize);
        out.flip();
        x.put(out);
        out.clear();
        out = x;
        return this;
    }

    /**
     * Truncate.
     *
     * @param newSize the new size
     * @return the output buffer
     */
    public OutputBuffer truncate(int newSize) {
        if (newSize < 0 || newSize > out.capacity()) {
            throw new IllegalArgumentException("Invalid newsize, must be >= 0");
        }
        if (newSize == out.capacity()) {
            return this;
        }
        byte[] bytes = new byte[0];
        ByteBuffer x = ByteBuffer.allocate(newSize);
        out.flip();
        out.get(bytes, 0, bytes.length);
        x.put(bytes);
        out.clear();
        out = x;
        return this;
    }

    /**
     * Write byte output buffer.
     *
     * @param b the b
     * @return the output buffer
     */
    public OutputBuffer writeByte(int b) {
        return writeByte(b, true);
    }

    /**
     * Write byte output buffer.
     *
     * @param b the b
     * @return the output buffer
     */
    private OutputBuffer writeByte(int b, boolean resetBitIndex) {
        if (out.remaining() < 1) {
            widen(out.capacity() + increaseSizeBytes);
        }
        out.put((byte) b);
        if (resetBitIndex) {
            bitIndex = 0;
        }
        return this;
    }

    /**
     * Write bit output buffer.
     *
     * @param value the value
     * @return the output buffer
     */
    public OutputBuffer writeBit(boolean value) {
        writeBits(value ? 1 : 0, 1);
        return this;
    }

    /**
     * Write bit output buffer.
     *
     * @param value the value
     * @return the output buffer
     */
    public OutputBuffer writeBit(int value) {
        writeBits(value, 1);
        return this;
    }

    /**
     * Write bits output buffer.
     *
     * @param value  the value
     * @param amount the amount
     * @return the output buffer
     */
    public OutputBuffer writeBits(long value, int amount) {
        if (bitIndex != 0) {
            int remainingBits = 8 - bitIndex;
            int bytePos = out.position() - 1;
            byte current = out.get(bytePos);
            int shiftAmount = amount - remainingBits;

            if (shiftAmount < 0) {
                out.put(bytePos, (byte) (current | (value << remainingBits - amount)));
            } else {
                out.put(bytePos, (byte) (current | (value >> shiftAmount)));
            }

            int bitsWritten = amount < remainingBits ? amount : remainingBits;
            bitIndex += bitsWritten;
            amount -= bitsWritten;
            if (bitIndex >= 8) {
                bitIndex = 0;
            }
        }
        if (amount <= 0) {
            return this;
        }
        bitIndex = amount & 7;
        int newAmount = amount - bitIndex;
        long newValue = (value >> bitIndex);
        for (; newAmount >= 8; newAmount -= 8) {
            writeByte((byte) (newValue >> newAmount), false);
        }
        writeByte((byte) (value << (8 - bitIndex)), false);
        return this;
    }


    /**
     * Create byte reserve buffer reserve.
     *
     * @param numBytes the num bytes
     * @return the buffer reserve
     */
    public IBufferReserve createByteReserve(int numBytes) {
        return new OutputBufferReserve(numBytes);
    }

    /**
     * Get bit position int.
     *
     * @return the int
     */
    public int getBitPosition() {
        return (out.position() * 8) + bitIndex;
    }

    /**
     * Write byte output buffer.
     *
     * @param b    the b
     * @param type the type
     * @return the output buffer
     */
    public OutputBuffer writeByte(int b, ByteTransformationType type) {
        return writeByte(transformValue(b, type));
    }

    /**
     * Write bytes output buffer.
     *
     * @param bytes the bytes
     * @return the output buffer
     */
    public OutputBuffer writeBytes(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }
        for (int i = 0; i < bytes.length; i++) {
            writeByte(bytes[i]);
        }
        return this;
    }

    /**
     * Writes the bytes in reverse.
     *
     * @param bytes the bytes
     * @return the output buffer
     */
    public OutputBuffer writeBytesReverse(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }
        for (int i = bytes.length - 1; i >= 0; i--) {
            writeByte(bytes[i]);
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
    public OutputBuffer writeBytes(int val, int repeat) {
        if (repeat <= 0) {
            throw new IllegalArgumentException("repeat cannot be > 0");
        }
        for (int i = 0; i < repeat; i++) {
            writeByte((byte) val);
            System.out.println("writing byte");
        }
        return this;
    }

    /**
     * Transform a value.
     */
    private byte transformValue(long value, ByteTransformationType type) {
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
    private byte[] longToByteArray(long value, int numBytes, ByteTransformationType type) {
        if (numBytes < 1 || numBytes > 8) {
            throw new RuntimeException("Invalid params, numBytes must be between 1-8 inclusive");
        }

        int start = 0;
        int end = 0;
        int increment = 0;

        switch (currentByteOrder) {
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
        byte[] bytes = new byte[numBytes];
        for (int i = start; i != end; i += increment) {
            if (i == 0) {
                transformValue(value, type);
            }

            bytes[bytesWritten++] = (byte) (value >> (i * 8));

            if (bytesWritten == numBytes) {
                break;
            }

            if (i == end + 1 && currentByteOrder == Order.BIG_MIDDLE_ENDIAN) {
                i = numBytes;
            }

            if (i == end - 1 && currentByteOrder == Order.LITTLE_MIDDLE_ENDIAN) {
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
    public OutputBuffer writeBytes(long value, int numBytes, ByteTransformationType type) {
        byte[] longBytes = longToByteArray(value, numBytes, type);
        for (byte b : longBytes) {
            writeByte(b);
        }
        return this;
    }

    /**
     * Out order output buffer.
     *
     * @param order the order
     * @return the output buffer
     */
    public OutputBuffer order(Order order) {
        currentByteOrder = order;
        return this;
    }

    /**
     * Write little dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleDWORD(long x) {
        return order(Order.LITTLE_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write big dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigDWORD(long x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write big qword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigQWORD(long x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 8, ByteTransformationType.NONE);
    }

    /**
     * Write little qword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleQWORD(long x) {
        return order(Order.LITTLE_ENDIAN).writeBytes(x, 8, ByteTransformationType.NONE);
    }

    /**
     * Write big word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigWORD(int x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * Write big word output buffer.
     * <p>
     * Transforms the LSB with +128 using ByteTransformationType.A
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigWORDA(int x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.A);
    }

    /**
     * Write little word output buffer.
     * <p>
     * Transforms the LSB with +128 using ByteTransformationType.A
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleWORDA(int x) {
        return order(Order.LITTLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.A);
    }

    /**
     * Write little word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleWORD(int x) {
        return order(Order.LITTLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * Write big middle dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigMiddleDWORD(long x) {
        return order(Order.BIG_MIDDLE_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write little middle dword output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleMiddleDWORD(long x) {
        return order(Order.LITTLE_MIDDLE_ENDIAN).writeBytes(x, 4, ByteTransformationType.NONE);
    }

    /**
     * Write little middle word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleMiddleWORD(int x) {
        return order(Order.LITTLE_MIDDLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * Write big middle word output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigMiddleWORD(int x) {
        return order(Order.BIG_MIDDLE_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * To array byte [ ].
     * <p>
     * Returns a byte array which backs this @class OutputBuffer.
     * <p>
     * Changes to the byte[] will affect the contents of this OutputBuffer.
     *
     * @return the byte [ ]
     */
    @Override
    public byte[] toArray() {
        return out.array();
    }

    /**
     * Returns a new @class ByteBuffer with the contents of this @class OutputBuffer.
     * Any operations on the ByteBuffer will not affect this @class OutputBuffer.
     * (Changes to the buffers contents will affect, however this OutputBuffer pos/limit/capcity etc will not be)
     * <p>
     * The returned @class ByteBuffer will have a position of 0 and limit/capacity of ByteBuffer.size().
     * Writing to the returned ByteBuffer will overwrite the data stored within.
     * <p>
     * This method should be used when another library only accepts a ByteBuffer as a parameter
     * and you don't intend on writing anymore data.
     *
     * @return the byte buffer
     */
    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(toArray());
    }

    @Override
    public int position() {
        return out.position();
    }

    @Override
    public void rewind() {
        out.rewind();
    }

    @Override
    public void skip(int numBytes) {
        out.position(out.position() + numBytes);
    }

    @Override
    public int remaining() {
        return out.remaining();
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
        void writeValue(long value, ByteTransformationType type);

        /**
         * Remaining int.
         *
         * @return the int
         */
        int remaining();
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
        public OutputBufferReserve(int numBytes) {
            reserveIndex = out.position();
            this.numBytes = numBytes;
            for (int i = 0; i < numBytes; i++) {
                writeByte(0);
            }
        }

        @Override
        public void writeValue(long value) {
            writeValue(value, ByteTransformationType.NONE);
        }

        @Override
        public void writeByte(int b) {
            out.put(reserveIndex + cursor++, (byte) b);
        }

        @Override
        public void writeBytes(ByteBuffer b) {
            Objects.requireNonNull(b);
            while (b.remaining() != 0 && remaining() != 0) {
                writeByte(b.get());
            }
        }

        @Override
        public void writeBytes(Byte[] bytes) {
            Objects.requireNonNull(bytes);
            for (int i = 0; i < bytes.length && remaining() != 0; i++) {
                writeByte(bytes[i]);
            }
        }

        @Override
        public void writeValue(long value, ByteTransformationType type) {
            byte[] bytes = longToByteArray(value, numBytes, type);
            for (byte b : bytes) {
                writeByte(b);
            }
        }

        @Override
        public int remaining() {
            return numBytes - cursor;
        }
    }
}