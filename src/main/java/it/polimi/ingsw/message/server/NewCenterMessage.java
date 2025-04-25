package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;

public class NewCenterMessage extends ServerMessage {
    
    private final ShipCoords new_center;

    public NewCenterMessage(ShipCoords new_center){
        if(new_center == null) throw new NullPointerException();
        this.new_center = new_center;
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
        state.setNewShipCenter(this.descriptor.getPlayer(), new_center);
    }

}
