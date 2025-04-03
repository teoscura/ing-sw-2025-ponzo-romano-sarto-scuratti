package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.MeteorMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class MeteorAnnounceState extends CardState {

    private final ProjectileArray left;
    private final List<Player> awaiting;
    private boolean broke_cabin;

    public MeteorAnnounceState(VoyageState state, ProjectileArray array){
        super(state);
        if(array==null) throw new NullPointerException();
        this.left = array;
        this.awaiting = this.state.getOrder(CardOrder.NORMAL);
    }

    @Override
    public void init() {
        super.init();
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            p.getDescriptor().sendMessage(new MeteorMessage(this.left.getProjectiles().getFirst()));
        }
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!awaiting.isEmpty()) return;
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            this.broke_cabin = p.getSpaceShip().handleMeteorite(this.left.getProjectiles().getFirst());
        }
        this.transition();
    }

    @Override
    protected CardState getNext(){
        if(broke_cabin) return new MeteorNewCabinState(state, left);
        this.left.getProjectiles().removeFirst();
        if(!this.left.getProjectiles().isEmpty()) return new MeteorAnnounceState(state, left);
        return null;
    }

    @Override
    public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords){
        if(!this.awaiting.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else"));
            return;
        }
        try{
            p.getSpaceShip().turnOn(target_coords, battery_coords);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Coords are not valid for the turnOn operation!"));
            return;
        }
    } 

    @Override
    public void progressTurn(Player p){
        if(!this.awaiting.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else"));
            return;
        }
        this.awaiting.remove(p);
    }
    
}
