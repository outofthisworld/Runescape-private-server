package net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class OutputBuffer {
    private static final int INITIAL_SIZE_BYTES = 8500;
    private static final int INCREASE_SIZE_BYTES = 4000;
    private ByteBuffer currentOutputBuffer;

    private OutputBuffer() {
        this(OutputBuffer.INITIAL_SIZE_BYTES);
    }

    private OutputBuffer(int initialSize) {
        currentOutputBuffer = ByteBuffer.allocate(initialSize);
    }

    private OutputBuffer(byte[] bytes) {
        currentOutputBuffer = ByteBuffer.allocate(bytes.length + OutputBuffer.INITIAL_SIZE_BYTES);
        currentOutputBuffer.put(bytes);
    }

    public static OutputBuffer create() {
        return new OutputBuffer();
    }

    int pipeAllTo(SocketChannel c) throws IOException {
        int bytesWritten = 0;
        int length = currentOutputBuffer.position();
        System.out.println("writing " + length + " bytes");
        while (bytesWritten != length) {
            bytesWritten += pipeTo(c);
        }

        System.out.println("wrote : " + bytesWritten);
        return bytesWritten;
    }

    int pipeTo(byte[] byteArr) {
        currentOutputBuffer.flip();
        currentOutputBuffer.get(byteArr, 0, byteArr.length);
        currentOutputBuffer.compact();
        return byteArr.length;
    }

    int pipeTo(SocketChannel c) throws IOException {
        currentOutputBuffer.flip();
        int bytesWritten = c.write(currentOutputBuffer);
        currentOutputBuffer.compact();
        return bytesWritten;
    }

    public OutputBuffer pipeTo(ByteBuffer b) throws Exception {
        currentOutputBuffer.flip();
        if (b.capacity() - b.position() < currentOutputBuffer.limit()) {
            throw new Exception("Not enough room in buffer b");
        }

        if (b.limit() != b.capacity()) {
            throw new Exception("Buffer may be in read mode");
        }

        b.put(currentOutputBuffer);
        currentOutputBuffer.compact();
        return this;
    }

    public int size() {
        return currentOutputBuffer.position();
    }


    void clear() {
        currentOutputBuffer.clear();
    }

    public OutputBuffer writeByte(int b) {
        if (currentOutputBuffer.position() + 1 >= currentOutputBuffer.capacity()) {
            ByteBuffer x = ByteBuffer.allocate(currentOutputBuffer.capacity() + OutputBuffer.INCREASE_SIZE_BYTES);
            currentOutputBuffer.flip();
            x.put(currentOutputBuffer);
            currentOutputBuffer.clear();
            currentOutputBuffer = x;
        }
        currentOutputBuffer.put((byte) b);
        return this;
    }

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

    public OutputBuffer writeBytes(long value, int numBytes) {

        if (numBytes < 1 || numBytes > 8) {
            throw new RuntimeException("Invalid params, numBytes must be between 1-8 inclusive");
        }
        int start;
        int end;
        int increment;

        ByteOrder currentOrder = currentOutputBuffer.order();

        if (currentOrder == ByteOrder.BIG_ENDIAN) {
            start = numBytes - 1;
            end = -1;
            increment = -1;
        } else {
            start = 0;
            end = numBytes;
            increment = 1;
        }

        for (int i = start; i != end; i += increment) {
            System.out.println(i * 8);
            writeByte((byte) (value >> (i * 8)));
        }

        return this;
    }

    private OutputBuffer outOrder(ByteOrder order) {
        currentOutputBuffer.order(order);
        return this;
    }

    public OutputBuffer writeLittleDWORD(long x) {
        return outOrder(ByteOrder.LITTLE_ENDIAN).writeBytes(x, 4);
    }

    public OutputBuffer writeBigDWORD(long x) {
        return outOrder(ByteOrder.BIG_ENDIAN).writeBytes(x, 4);
    }

    public OutputBuffer writeBigQWORD(long x) {
        return outOrder(ByteOrder.BIG_ENDIAN).writeBytes(x, 8);
    }

    public OutputBuffer writeLittleQWORD(long x) {
        return outOrder(ByteOrder.LITTLE_ENDIAN).writeBytes(x, 8);
    }


    public OutputBuffer writeBigWORD(int x) {
        return outOrder(ByteOrder.BIG_ENDIAN).writeBytes(x, 2);
    }

    public OutputBuffer writeLittleWORD(int x) {
        return outOrder(ByteOrder.LITTLE_ENDIAN).writeBytes(x, 2);
    }
}