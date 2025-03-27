package it.polimi.ingsw.server;

import java.util.HashMap;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.net.RMIHandler;
import it.polimi.ingsw.net.TCPHandler;

public class ServerController {
    //Connections
    private RMIHandler rmi_handler;
    private TCPHandler tcp_handler;
    //Data Storage
    private HashMap<Long, MatchController> match_list;
    private HashMap<Long, String> unfinished_games;
    private HashMap<String, ClientDescriptor> player_list;

    public ServerController(String ip, short port, String url){
        //TODO: create tcp and rmi handlers.
    }

    public void getUnfinishedMatches(){
        //FIXME change signature into a coherent one.
        //TODO implement.
    }

    public HashMap<Long, MatchController> getOpenWaitingRooms(){
        //TODO implement.
        return null;
    }

    public void serverLoop(){
        //XXX URGENT URGENT URGENT.
    }

}
