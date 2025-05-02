package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.message.client.ClientMessage;

public class DummyController extends ServerController {
    
    @Override
    public void endGame(){
        return;
    }

    @Override
    public void serializeCurrentGame(){
        return;
    }

    @Override
    public void broadcast(ClientMessage message){
        return;
    }
}
