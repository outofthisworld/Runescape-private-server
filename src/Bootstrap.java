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
    }
}
