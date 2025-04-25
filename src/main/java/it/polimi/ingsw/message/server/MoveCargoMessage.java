package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;

public class MoveCargoMessage extends ServerMessage {
    
    private final ShipCoords target;
    private final ShipCoords source;
    private final ShipmentType type;

    public MoveCargoMessage(ShipCoords target, ShipCoords source, ShipmentType type){
        if(target==null||source==null||type.getValue()<1) throw new NullPointerException();
        this.target = target;
        this.source = source;
        this.type = type;
    }

    @Override
    public void receive(ServerController server) throws ForbiddenCallException {
        if(descriptor.getPlayer()==null) throw new ForbiddenCallException("Descriptor associated to message isn't bound to player");
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
        state.moveCargo(this.descriptor.getPlayer(), type, target, source);
    }

}
