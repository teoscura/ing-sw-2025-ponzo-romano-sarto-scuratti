package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.ModelInstance;

public class DummyController extends LobbyController {

    public DummyController(int id, ModelInstance model) {
        super(id, model);
    }

    @Override
    protected void endGame(){
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
