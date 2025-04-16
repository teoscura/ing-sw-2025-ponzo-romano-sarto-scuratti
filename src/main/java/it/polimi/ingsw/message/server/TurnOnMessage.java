package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.controller.server.exceptions.PlayerUnsetException;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;

public class TurnOnMessage extends ServerMessage {

    private final ShipCoords target;
    private final ShipCoords battery;

    public TurnOnMessage(ClientDescriptor descriptor, ShipCoords target, ShipCoords battery){
        if(descriptor.getPlayer()==null) throw new PlayerUnsetException("Descriptor associated to message isn't bound to player");
        if(descriptor==null||target==null||battery==null) throw new NullPointerException();
        this.target = target;
        this.battery = battery;
    }

    @Override
    public void receive(ServerController server) throws ForbiddenCallException {
        server.getModel().validate(this);
    }

    @Override
    public void receive(ModelInstance instance) throws ForbiddenCallException {
        instance.getState().validate(this);
    }

    @Override
    public void receive(GameState state) throws ForbiddenCallException {
        state.getCardState(this.descriptor.getPlayer()).validate(this);
    }

    @Override
    public void receive(CardState state) throws ForbiddenCallException {
        state.turnOn(this.descriptor.getPlayer(), target, battery);
    }
    
}
