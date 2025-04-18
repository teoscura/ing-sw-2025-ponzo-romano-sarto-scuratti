package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.message.Message;

public abstract class ClientMessage implements Message {

    public abstract void receive(ClientController client);
}
