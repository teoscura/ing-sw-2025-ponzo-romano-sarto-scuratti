package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;

public class SetCrewMessage extends ServerMessage {
    
    private final ShipCoords coords;
    private final AlienType type;

    public SetCrewMessage(ShipCoords coords, AlienType type){
        if(coords == null || type.getArraypos() < 0) throw new NullPointerException();
        this.coords = coords;
        this.type = type;
    }

    @Override
    public void receive(ServerController server) throws ForbiddenCallException {
        if(this.descriptor.getPlayer()==null) throw new ForbiddenCallException("Descriptor associated to message isn't bound to player");
        server.getModel().validate(this);
    }

    @Override
    public void receive(ModelInstance instance) throws ForbiddenCallException {
        instance.getState().validate(this);
    }

    @Override
    public void receive(GameState state) throws ForbiddenCallException {
        state.setCrewType(this.descriptor.getPlayer(), coords, type);
    }

}
