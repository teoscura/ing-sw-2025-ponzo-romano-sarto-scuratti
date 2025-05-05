package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.server.rmi.RemoteServer;

public class MainServerController extends Thread implements RemoteServer {
    
    static private MainServerController instance = new MainServerController();

    private final Server server;

    private MainServerController(){
        this.server = new Server(this);
    }

    static public MainServerController getInstance(){
        return instance;
    }

}
