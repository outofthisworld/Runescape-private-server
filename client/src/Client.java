import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("localhost",3434));

            System.out.println(socketChannel.isConnected());

            String sendString = "Hello world this is a messageHello world this is a messageHello world this is a messageHello world this is a message" +
                    "Hello world this is a messageHello world this is a messageHello world this is a messageHello world this is a messageHello world this is a message" +
                    "Hello world this is a messageHello world this is a messageHello world this is a messageHello world this is a message" +
                    "Hello world this is a messageHello world this is a messageHello world this is a message";
            byte[] bytesToSend = sendString.getBytes();
            ByteBuffer b = ByteBuffer.allocate(5);
            b.put((byte) 255);
            b.putInt(bytesToSend.length);

            b.flip();
            int written = socketChannel.write(b);

            b.clear();

            b = ByteBuffer.allocate(bytesToSend.length);
            b.put(bytesToSend);
            b.flip();
            socketChannel.write(b);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
