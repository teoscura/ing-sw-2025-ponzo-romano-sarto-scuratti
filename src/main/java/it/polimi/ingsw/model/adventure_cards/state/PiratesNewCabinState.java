package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PiratesCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class PiratesNewCabinState extends CardState {

    private final PiratesCard card;
    private final List<Player> list;
    private final ProjectileArray shots;

    public PiratesNewCabinState(VoyageState state, PiratesCard card, List<Player> list, ProjectileArray shots) {
        super(state);
        if(card==null||list==null||shots==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
        this.shots = shots;
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(this.list.getFirst().getSpaceShip().getBrokeCenter()) return;
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.list.getFirst().getRetired()){
            this.list.removeFirst();
            if(!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
            return null;
        }
        this.shots.getProjectiles().removeFirst();
        if(!this.shots.getProjectiles().isEmpty()) return new PiratesPenaltyState(state, card, list, shots);
        this.list.removeFirst();
        if(!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
        return null;
    }
    
    @Override
    public void setNewShipCenter(Player p, ShipCoords new_center){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        try{
            p.getSpaceShip().setCenter(new_center);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Target is an empty space!"));
        } catch (ForbiddenCallException e){
            //Should never get here.
            p.getDescriptor().sendMessage(new ViewMessage("Cabin isn't broken!"));
        }
    }

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.state.loseGame(p);
            this.transition();
        }
        if(this.list.contains(p)){
            this.list.remove(p);
        }
        //XXX controllare se va bene.
    }

}
