package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;

public interface iServerController {

    //ServerController ones.
    public void connect(ClientDescriptor client) throws ForbiddenCallException;
    public void disconnect(ClientDescriptor client) throws ForbiddenCallException;
    public void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count) throws ForbiddenCallException ;
    public void getUnfinishedList(ClientDescriptor client) throws ForbiddenCallException; 
    public void getMyUnfinishedList(ClientDescriptor client) throws ForbiddenCallException;
    public void openUnfinished(ClientDescriptor client, int id) throws ForbiddenCallException;
    public void ping(ClientDescriptor client) throws ForbiddenCallException ;
}
