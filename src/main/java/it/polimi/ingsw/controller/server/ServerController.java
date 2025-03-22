package it.polimi.ingsw.controller.server;

import java.util.HashMap;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.controller.match.MatchSettings;
import it.polimi.ingsw.controller.net.RMIHandler;
import it.polimi.ingsw.controller.net.TCPHandler;

public class ServerController {
    //Connections
    private RMIHandler rmi_handler;
    private TCPHandler tcp_handler;
    //Data Storage
    private HashMap<Long, MatchController> match_list;
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

    private void connectToRoom(){
        //FIXME change signature into a coherent one.
        //TODO implement.
    }

    private void connectToUnfinished(){
        //HACK i think this one is basically redundant.
    }

    private void openRoom(MatchSettings settings){
        //FIXME change signature into a coherent one.
        //TODO implement.
    }

    private void retrieveMessages(){
        //TODO.
    }

    //FIXME these methods are unfinished.

}
