package it.polimi.ingsw.controller.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.controller.client.RMIServerStub;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;

public class Server extends Thread implements iRMIStubProvider {
    
    static private final Server instance = new Server();
    private final ExecutorService serverPool;
    private final ExecutorService messagePool; 
    private String ip = "localhost";
    private ServerController controller = null;

    private Server(){
        this.serverPool = new ThreadPoolExecutor(6, 20, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));
        this.messagePool = new ThreadPoolExecutor(10, 100, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));
    }

    public void setController(ServerController controller){
        this.controller = controller;
    }

    public void run(){
        if(controller==null) throw new RuntimeException();
        if(System.console()!=null){
            System.out.println("Insert the desired server address [default: localhost]");
            ip = System.console().readLine();
        }
        //XXX
    }

    static public Server getInstance(){
        return instance;
    }

    public RMIServerStub accept(RMIClientStub client){
        return this.controller.getStub(client);
    }

}
