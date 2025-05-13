package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.message.client.ClientMessage;

public class DummyController extends LobbyController {

    public DummyController(int id) {
        super(id);
    }

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
