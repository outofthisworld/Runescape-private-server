package net.packets;

import net.ISocketHandler;
import net.SocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SocketManager {

    private static Executor e = Executors.newCachedThreadPool();
    private ArrayList<ISocketHandler> socketHandlers = new ArrayList();

    private SocketManager(){

    }

    public static SocketManager create() throws IOException {
        return SocketManager.create(Runtime.getRuntime().availableProcessors());
    }

    public static SocketManager create(int numHandlers) throws IOException {
        SocketManager s = new SocketManager();
        for(int i = 0; i < numHandlers;i++){
            new SocketHandler()
            s.addSocketHandler(new SocketHandler());
        }
    }

    public <T extends ISocketHandler> void addSocketHandler(T t){
        if(!t.isRunning()){
            e.execute(t);
        }
        socketHandlers.add(t);
    }



}
