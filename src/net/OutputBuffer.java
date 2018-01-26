package net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class OutputBuffer {
    private static final int INITIAL_SIZE_BYTES = 8500;
    private static final int INCREASE_SIZE_BYTES = 4000;
    private ByteBuffer currentOutputBuffer;

    private OutputBuffer() {
        this(INITIAL_SIZE_BYTES);
    }

    private OutputBuffer(int initialSize) {
        currentOutputBuffer = ByteBuffer.allocate(initialSize);
    }

    private OutputBuffer(byte[] bytes) {
        currentOutputBuffer = ByteBuffer.allocate(bytes.length + INITIAL_SIZE_BYTES);
        currentOutputBuffer.put(bytes);
    }

    public static OutputBuffer create() {
        return new OutputBuffer();
    }

    int pipeAllTo(SocketChannel c) {

    }

    int pipeAllTo(Client c) throws IOException {
        return pipeAllTo(c.getSocket());
    }

    int pipeTo(SocketChannel c) throws IOException {
        int bytesWritten = c.write(currentOutputBuffer);
        currentOutputBuffer.compact();
        return bytesWritten;
    }

    int pipeTo(Client c) throws IOException {
        return pipeTo(c.getSocket());
    }

    void clear() {
        currentOutputBuffer.clear();
    }

    public OutputBuffer writeByte(byte b) {
        if (currentOutputBuffer.position() + 1 >= currentOutputBuffer.capacity()) {
            ByteBuffer x = ByteBuffer.allocate(currentOutputBuffer.capacity() + INCREASE_SIZE_BYTES);
            currentOutputBuffer.flip();
            x.put(currentOutputBuffer);
            currentOutputBuffer.clear();
            currentOutputBuffer = x;
        }
        currentOutputBuffer.put(b);
        return this;
    }

    public OutputBuffer writeBigDWORD(long x) {
        for (int i = 3; i >= 0; i--) {
            writeByte((byte) (x >> (i * 8)));
        }
        return this;
    }

    public OutputBuffer writeBigQWORD(long x) {
        for (int i = 3; i >= 0; i--) {
            writeByte((byte) (x >> (i * 8)));
        }
        return this;
    }

    public OutputBuffer writeLittleQWORD(long x) {
        for (int i = 3; i >= 0; i--) {
            writeByte((byte) (x >> (i * 8)));
        }
        return this;
    }

    public OutputBuffer writeLittleDWORD(long x) {
        for (int i = 0; i >= 3; i++) {
            writeByte((byte) (x >> (i * 8)));
        }
        return this;
    }

    public OutputBuffer writeBigWORD(int x) {
        for (int i = 0; i >= 3; i++) {
            writeByte((byte) (x >> (i * 8)));
        }
        return this;
    }

    public OutputBuffer writeLittleWORD(int x) {
        for (int i = 0; i >= 3; i++) {
            writeByte((byte) (x >> (i * 8)));
        }
        return this;
    }
}
