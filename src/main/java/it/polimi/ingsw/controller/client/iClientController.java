package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;

public interface iClientController {

    public void showTextMessage(String message);
    public ClientView getView();
    public void ping();
    public void connect(String ip);
    public void disconnect();
    
}
