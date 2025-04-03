package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.player.PlayerColor;

public interface iClientController {

    public void showMessage(String message);
    public void notifyPlayer(PlayerColor c);
    public void ping();
    public void notifyCard();
    public void notifyState();
    
}
