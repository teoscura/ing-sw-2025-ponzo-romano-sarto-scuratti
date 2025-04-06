package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.client.NotifyCardStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public abstract class CardState {

    protected final VoyageState state;

    protected CardState(VoyageState state){
        if(state==null) throw new NullPointerException();
        this.state = state;
    }

    public void init(){
        for(Player p : state.getOrder(CardOrder.NORMAL)){
            p.getSpaceShip().resetPower();
            p.getDescriptor().sendMessage(new NotifyCardStateUpdateMessage());
        }
    };

    public abstract void validate(ServerMessage message) throws ForbiddenCallException;

    protected abstract CardState getNext();

    public void transition(){
        this.state.setCardState(this.getNext());
    }

    public void setNewShipCenter(Player p, ShipCoords new_center) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void removeCargo(Player p, ShipmentType shipment, ShipCoords target_coords) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void takeCargo(Player p, ShipmentType type, ShipCoords storage_coords) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void discardCargo(Player p, ShipmentType type, ShipCoords target_coords) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void selectLanding(Player p, int planet) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void progressTurn(Player p) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }
    
    public void setTakeReward(Player p, boolean take) throws ForbiddenCallException{
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void connect(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void disconnect(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

}
