package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class ViewMessage extends ClientMessage {

    private final String message;

    public ViewMessage(String message){
        if(message==null) throw new NullPointerException();
        this.message = message;
    }

    @Override
    public void receive(iClientController client) {
        client.showTextMessage(message);
    }

}
