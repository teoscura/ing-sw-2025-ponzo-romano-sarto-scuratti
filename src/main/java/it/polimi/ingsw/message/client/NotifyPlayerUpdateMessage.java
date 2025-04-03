package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;
import it.polimi.ingsw.model.player.PlayerColor;

public class NotifyPlayerUpdateMessage extends ClientMessage {

    private final PlayerColor c;

    public NotifyPlayerUpdateMessage(PlayerColor c){
        if(c.getOrder()<0) throw new IllegalArgumentException();
        this.c = c;
    }

    @Override
    public void receive(iClientController client) {
        client.notifyPlayer(c);
    }
}
