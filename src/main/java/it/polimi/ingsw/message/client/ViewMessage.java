package it.polimi.ingsw.message.client;

public class ViewMessage extends ClientMessage {

    private final String message;

    public ViewMessage(String message){
        if(message==null) throw new NullPointerException();
        this.message = message;
    }

}
