package net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class InputBuffer {
    private final ByteBuffer inBuffer;

    public InputBuffer(byte[] bytes){
        inBuffer = ByteBuffer.allocate(bytes.length);
        inBuffer.flip();
    }

    private long readBytes(int numBytes){

        if(numBytes < 1 || numBytes > 8){
            throw new RuntimeException("Invalid params, numBytes must be between 1-8 inclusive");
        }

        byte[] bytes = new byte[numBytes];
        inBuffer.get(bytes,0,bytes.length);
        long l = 0L;

        int start;
        int end;
        int increment;

        if(inBuffer.order() == ByteOrder.BIG_ENDIAN){
            start = 0;
            end = numBytes;
            increment = 1;
        }else{
            start = numBytes;
            end = 0;
            increment = -1;
        }

        for(int i = start;i!=end;i+=increment){
            l|= (bytes[i] << ((numBytes-1-i)*8));
        }
        inBuffer.compact();
        return l;
    }

    private InputBuffer inOrder(ByteOrder b){
        inBuffer.order(b);
        return this;
    }

    public byte readSignedByte(){
        return inBuffer.get();
    }

    public short readUnsignedByte(){
        return (short) (inBuffer.get() & 0xFF);
    }


    public long readBigUnsignedDWORD(){
        long bytes = inOrder(ByteOrder.BIG_ENDIAN).readBytes(4);
        return bytes & 0xFFFFFFFFL;
    }

    public long readLittleUnsignedDWORD(){
        long bytes = inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(4);
        return bytes & 0xFFFFFFFFL;
    }

    public int readBigSignedDWORD(){
        return (int) inOrder(ByteOrder.BIG_ENDIAN).readBytes(4);
    }

    public int readLittleSignedDWORD(){
        return (int) inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(4);
    }

    public short readLittleSignedWORD(){
        return (short) inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(2);
    }

    public short readBigSignedWORD(){
        return (short) inOrder(ByteOrder.BIG_ENDIAN).readBytes(2);
    }

    public int readLittleUnsignedWORD(){
        return (int) (inOrder(ByteOrder.LITTLE_ENDIAN).readBytes(2) & 0xFFFFL);
    }

    public int readBigUnsignedWORD(){
        return (int) (inOrder(ByteOrder.BIG_ENDIAN).readBytes(2) & 0xFFFFL);
    }
}
