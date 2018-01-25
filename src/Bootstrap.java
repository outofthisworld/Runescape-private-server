import net.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Bootstrap {
    public static void main(String[] args) {
        Server server = new Server(ServerConfig.HOST, ServerConfig.PORT);

        Thread serverThread = new Thread(() -> {
            boolean serverStarted = false;
            while (!serverStarted) {
                try {
                    server.start();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });

        serverThread.setName("");
        serverThread.setPriority(Thread.MAX_PRIORITY);
        serverThread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Uncaught exception : ");
            e.printStackTrace();
        });
        serverThread.start();

        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open(server.getINetAddress());

            socketChannel.finishConnect();

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
