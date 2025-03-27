package it.polimi.ingsw.controller.message;

public interface Message {
    public void sendTo(iGameMessageHandler m) throws Exception;
    public void sendTo(iServerMessageHandler m) throws Exception;
}
