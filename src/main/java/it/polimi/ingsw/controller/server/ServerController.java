package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
public class ServerController implements iServerController {
    
    private ModelInstance model = null;
    private Server server = Server.getInstance();
    private boolean started = false;
    
    public ModelInstance getModel() throws ForbiddenCallException {
        if(this.model==null) throw new ForbiddenCallException();
        return this.model;
    }

    @Override
    public void connect(ClientDescriptor client) {
        XXX
    }

    @Override
    public void disconnect(ClientDescriptor client) {
        XXX
    }

    @Override
    public void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count) {
        XXX
    }

    @Override
    public void getUnfinishedList(ClientDescriptor client) {
        XXX
    }

    @Override
    public void getMyUnfinishedList(ClientDescriptor client) {
        XXX
    }

    @Override
    public void openUnfinished(ClientDescriptor client, int id) {
        XXX
    };

    @Override
    public void ping(ClientDescriptor client) {
        XXX
    }

}
