package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class MoveOnBoardMessage extends ClientMessage {
    
    private final int days;

    public MoveOnBoardMessage(int days){
        this.days = days;
    }

    @Override
    public void recieve(iClientController client) {
        client.moveOnBoard(days);
    }
}
