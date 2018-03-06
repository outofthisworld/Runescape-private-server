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
import util.integrity.Preconditions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * The type Output buffer.
 */
public class OutputBuffer extends AbstractBuffer {
    private static final int INITIAL_SIZE = 512;
    private static final int INCREASE_SIZE_BYTES = 512;
    private final int increaseSizeBytes;
    private ByteBuffer out;
    private Order currentByteOrder = Order.BIG_ENDIAN;
    private int bitIndex = 0;


    private IBufferReserve<OutputBuffer> lastReserve = null;

    private OutputBuffer() {
        this(OutputBuffer.INITIAL_SIZE, OutputBuffer.INCREASE_SIZE_BYTES);
    }

    private OutputBuffer(int initialSize, int increaseSizeBytes) {
        this(ByteBuffer.allocate(initialSize), increaseSizeBytes);
    }

    private OutputBuffer(byte[] bytes, int increaseSizeBytes) {
        this(ByteBuffer.allocate(bytes.length).put(bytes), increaseSizeBytes);
    }

    private OutputBuffer(byte[] bytes) {
        this(bytes, OutputBuffer.INCREASE_SIZE_BYTES);
    }

    private OutputBuffer(ByteBuffer src, int increaseSizeBytes) {
        assert src.limit() == src.capacity();
        this.increaseSizeBytes = increaseSizeBytes;
        out = src;
    }

    private OutputBuffer(ByteBuffer src) {
        this(src, OutputBuffer.INCREASE_SIZE_BYTES);
    }


    /**
     * Create output buffer.
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
     * @param increaseSizeBytes the increase size bytes
     * @return the output buffer
     */
    public static OutputBuffer create(int initialSize, int increaseSizeBytes) {
        return new OutputBuffer(initialSize, increaseSizeBytes);
    }

    /**
     * Create output buffer.
     *
     * @param initialSize the initial size
     * @return the output buffer
     */
    public static OutputBuffer create(int initialSize) {
        return new OutputBuffer(initialSize, OutputBuffer.INCREASE_SIZE_BYTES);
    }

    /**
     * Wrap output buffer.
     *
     * @param bytes             the bytes
     * @param increaseSizeBytes the increase size bytes
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
     * Wrap output buffer.
     *
     * @param src the src
     * @return the output buffer
     */
    public static OutputBuffer wrap(ByteBuffer src) {
        return new OutputBuffer(src);
    }

    /**
     * Wrap output buffer.
     *
     * @param src               the src
     * @param increaseSizeBytes the increase size bytes
     * @return the output buffer
     */
    public static OutputBuffer wrap(ByteBuffer src, int increaseSizeBytes) {
        return new OutputBuffer(src, increaseSizeBytes);
    }

    /**
     * Pipe all to int.
     *
     * @param c the c
     * @return the int
     * @throws IOException the io exception
     */
    public int pipeAllTo(SocketChannel c) throws IOException {
        Objects.requireNonNull(c);
        int bytesWritten = 0;
        int length = out.position();
        while (bytesWritten != length) {
            bytesWritten += pipeTo(c);
        }
        return bytesWritten;
    }

    /**
     * Pipe to int.
     *
     * @param byteArr the byte arr
     * @return the int
     */
    public int pipeTo(byte[] byteArr) {
        return pipeTo(byteArr, true);
    }

    /**
     * Pipe to int.
     *
     * @param byteArr the byte arr
     * @param compact the compact
     * @return the int
     */
    public int pipeTo(byte[] byteArr, boolean compact) {
        Objects.requireNonNull(byteArr);
        out.flip();
        out.get(byteArr, 0, byteArr.length);
        if (compact) {
            out.compact();
        }
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
        return pipeTo(c, true);
    }

    /**
     * Pipe to int.
     *
     * @param c       the c
     * @param compact the compact
     * @return the int
     * @throws IOException the io exception
     */
    public int pipeTo(SocketChannel c, boolean compact) throws IOException {
        Objects.requireNonNull(c);
        out.flip();
        int bytesWritten;
        try {
            bytesWritten = c.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            bytesWritten = -1;
        }
        if (compact) {
            out.compact();
        }
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
        pipeTo(b, true);
        return this;
    }

    /**
     * Pipe to output buffer.
     *
     * @param b       the b
     * @param compact the compact
     * @return the output buffer
     * @throws Exception the exception
     */
    public OutputBuffer pipeTo(ByteBuffer b, boolean compact) throws Exception {
        Objects.requireNonNull(b);
        out.flip();
        assert b.limit() == b.capacity();
        if (b.remaining() < out.remaining()) {
            throw new Exception("Not enough room in buffer b");
        }
        b.put(out);
        if (compact) {
            out.compact();
        }
        return this;
    }

    /**
     * Pipe to output buffer.
     *
     * @param other the other
     * @return the output buffer
     */
    public OutputBuffer pipeTo(OutputBuffer other) {
        pipeTo(other, true);
        return this;
    }

    /**
     * Pipe to output buffer.
     *
     * @param other   the other
     * @param compact the compact
     * @return the output buffer
     */
    public OutputBuffer pipeToAsBits(OutputBuffer other, boolean compact) {
        Objects.requireNonNull(other);
        out.flip();
        while (out.remaining() != 0) {
            other.writeBits(out.get(), 8);
        }
        if (compact) {
            out.compact();
        }
        return this;
    }

    public int limit() {
        return out.limit();
    }

    public int capacity() {
        return out.capacity();
    }

    /**
     * Pipe to output buffer.
     *
     * @param other   the other
     * @param compact the compact
     * @return the output buffer
     */
    public OutputBuffer pipeTo(OutputBuffer other, boolean compact) {
        Objects.requireNonNull(other);
        out.flip();
        while (out.remaining() != 0) {
            other.writeByte(out.get());
        }
        if (compact) {
            out.compact();
        }
        return this;
    }

    @Override
    public int size() {
        return out.position();
    }

    @Override
    public void clear() {
        out.clear();
    }

    /**
     * Widen output buffer.
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
     * Truncate output buffer.
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
            int bitsWritten = amount < remainingBits ? amount : remainingBits;
            int clearShiftAmount = 8 - bitsWritten + 56;

            byte b;
            if (shiftAmount < 0) {
                b = (byte) (current | (value << remainingBits - amount));
            } else {
                long temp = (value >> shiftAmount);
                temp = (temp << clearShiftAmount);
                temp = (byte) (temp >>> clearShiftAmount);
                b = (byte) (current | temp);
            }
            out.put(bytePos, b);
            bitIndex = (bitIndex + bitsWritten) % 8;
            amount -= bitsWritten;
        }
        if (amount <= 0) {
            return this;
        }
        bitIndex = amount & 7;
        int newAmount = amount - bitIndex;
        //newValue should not equal 2047
        for (int i = 0; i != newAmount; i += 8) {
            writeByte((byte) ((value >> i)), false);
        }
        if (bitIndex > 0)
            writeByte((byte) (value << (8 - bitIndex)), false);
        return this;
    }


    /**
     * Create byte reserve buffer reserve.
     *
     * @param numBytes the num bytes
     * @return the buffer reserve
     */
    public IBufferReserve<OutputBuffer> createByteReserve(int numBytes) {
        if (numBytes <= 0 || out.position() + numBytes >= out.limit()) {
            throw new IllegalArgumentException("Invalid numBytes, must be > 0 && < out.limit");
        }
        return new OutputBufferReserve(numBytes);
    }

    /**
     * Gets bit position.
     *
     * @return the bit position
     */
    public int getBitPosition() {
        return (out.position() * 8) - (8 - bitIndex);
    }

    /**
     * Write byte output buffer.
     *
     * @param b    the b
     * @param type the type
     * @return the output buffer
     */
    public OutputBuffer writeByte(int b, ByteTransformationType type) {
        return writeByte(type.transformValue((byte) b));
    }


    /**
     * Write bytes output buffer.
     *
     * @param src the src
     * @return the output buffer
     */
    public OutputBuffer writeBytes(ByteBuffer src) {
        while (src.remaining() != 0) {
            writeByte(src.get());
        }
        return this;
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
     * Write bytes reverse output buffer.
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
        }
        return this;
    }


    private byte[] longToByteArray(long value, int numBytes, ByteTransformationType type) {
        Preconditions.inRangeClosed(numBytes, 1, 8);
        Preconditions.notNull(type);

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
            byte b = (byte) (value >> (i * 8));

            if (i == 0) {
                b = type.transformValue(b);
            }

            bytes[bytesWritten++] = b;

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
     * Order output buffer.
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
    public OutputBuffer writeBigWord(int x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.NONE);
    }

    /**
     * Write big worda output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigWordTypeA(int x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.A);
    }

    /**
     * Write big word type c output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigWordTypeC(int x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.C);
    }

    /**
     * Write big worda output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeBigWordTypeS(int x) {
        return order(Order.BIG_ENDIAN).writeBytes(x, 2, ByteTransformationType.S);
    }

    /**
     * Write little worda output buffer.
     *
     * @param x the x
     * @return the output buffer
     */
    public OutputBuffer writeLittleWordTypeA(int x) {
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
     * To underlying array byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] toUnderlyingArray() {
        return out.array();
    }

    /**
     * To last reserve buffer reserve.
     *
     * @return the buffer reserve
     */
    public IBufferReserve<OutputBuffer> toLastReserve() {
        return lastReserve;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer dup = toByteBuffer();
        dup.flip();
        byte[] bytes = new byte[dup.remaining()];
        dup.get(bytes);
        return bytes;
    }

    /**
     * Copy output buffer.
     *
     * @return the output buffer
     */
    public OutputBuffer copy() {
        byte[] current = out.array();
        byte[] dest = new byte[current.length];
        System.arraycopy(out.array(), 0, dest, 0, dest.length);
        OutputBuffer out = new OutputBuffer(dest);
        out.out.position(this.out.position());
        out.out.limit(this.out.limit());
        return out;
    }


    @Override
    public ByteBuffer toByteBuffer() {
        return out.duplicate();
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


    private class OutputBufferReserve implements IBufferReserve<OutputBuffer> {
        private final int reserveIndex;
        private final int numBytes;
        private int cursor = 0;

        /**
         * Instantiates a new Output buffer reserve.
         *
         * @param numBytes the num bytes
         */
        public OutputBufferReserve(int numBytes) {
            lastReserve = this;
            reserveIndex = out.position();
            this.numBytes = numBytes;
            for (int i = 0; i < numBytes; i++) {
                OutputBuffer.this.writeByte(0);
            }
        }

        @Override
        public IBufferReserve<OutputBuffer> writeValue(long value) {
            writeValue(value, ByteTransformationType.NONE);
            return this;
        }

        @Override
        public IBufferReserve<OutputBuffer> writeByte(int b) {
            if (remaining() == 0) {
                throw new InvalidStateException("Cannot exceed reserved bytes (" + numBytes + "). To many bytes written. In class " + getClass().getName());
            }
            out.put(reserveIndex + cursor++, (byte) b);
            return this;
        }

        @Override
        public IBufferReserve<OutputBuffer> writeBytes(ByteBuffer b) {
            Objects.requireNonNull(b);
            while (b.remaining() != 0 && remaining() != 0) {
                writeByte(b.get());
            }
            return this;
        }

        @Override
        public IBufferReserve<OutputBuffer> writeBytes(Byte[] bytes) {
            Objects.requireNonNull(bytes);
            for (int i = 0; i < bytes.length && remaining() != 0; i++) {
                writeByte(bytes[i]);
            }
            return this;
        }

        @Override
        public IBufferReserve<OutputBuffer> writeValue(long value, ByteTransformationType type) {
            byte[] bytes = longToByteArray(value, numBytes, type);
            for (byte b : bytes) {
                writeByte(b);
            }
            return this;
        }

        @Override
        public IBufferReserve<OutputBuffer> writeBytesSinceReserve() {
            writeValue(bytesSinceReserve());
            return this;
        }

        @Override
        public int bytesSinceReserve() {
            return ((out.position() - 1) - (reserveIndex + (numBytes - 1)));
        }

        @Override
        public OutputBuffer toBuffer() {
            return OutputBuffer.this;
        }

        @Override
        public int remaining() {
            return numBytes - cursor;
        }
    }
}