package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.player.PlayerColor;

public class ClientController implements iClientController {


    @Override
    public void showMessage(String message) {
        this.view.showMessage(message);
    }

    @Override
    public void notifyPlayer(PlayerColor c) {
        this.server.requestPlayer(c);
    }

    @Override
    public void ping() {
        this.server.ping();
    }

    @Override
    public void notifyCard() {
        this.server.askNewState();
        this.view.updateState();
    }
    
}
