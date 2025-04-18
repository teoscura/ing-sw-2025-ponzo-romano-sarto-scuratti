package it.polimi.ingsw.controller.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.controller.client.RMIClientStub;
import it.polimi.ingsw.controller.server.rmi.RMIServerSkeleton;
import it.polimi.ingsw.controller.server.rmi.RMISkeletonProvider;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;

public class Server extends Thread implements RMISkeletonProvider {
    
    private final ExecutorService serverPool;
    private final ServerController controller;
    private String ip = "localhost";

    public Server(ServerController controller){
        if(controller == null) throw new NullPointerException();
        this.controller = controller;
        this.serverPool = new ThreadPoolExecutor(6, 60, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));     
    }

    public void run(){
        if(controller==null) throw new RuntimeException();
        if(System.console()!=null){
            System.out.println("Insert the desired server address [default: localhost]");
            this.ip = System.console().readLine();
        }
        System.setProperty("java.rmi.server.hostname", this.ip);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(9999);
            registry.rebind("galaxy_truckers", this);
            UnicastRemoteObject.exportObject(this, 9999);
        } catch (RemoteException e) {
            throw new RuntimeException("Failed to setup the rmi registry and remote object, terminating.");
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket();
        } catch (IOException e) {
            throw new RuntimeException("Failed to setup the server socket, terminating.");
        }
        try{
            server.bind(new InetSocketAddress(this.ip, 10000));
            while (!this.controller.getEnded()) {
                SocketClient new_connection = new SocketClient(server.accept());
                this.controller.connectListener(new_connection);
                this.serverPool.submit(
                    () -> {
                        while(true) {
                            if(new_connection.getSocket().isClosed()){
                                return;
                            }
                            new_connection.read(this.controller);
                        }
                    }
                );
            }
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RMIServerSkeleton accept(RMIClientStub client){
        ClientDescriptor new_client = this.controller.connectListener(client);
        return this.controller.getStub(new_client);
    }

}
