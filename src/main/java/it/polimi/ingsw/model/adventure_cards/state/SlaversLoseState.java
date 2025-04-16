package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.EmptyMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SlaversCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCrewPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class SlaversLoseState extends CardState {

    private final SlaversCard card;
    private final List<Player> list;
    private boolean responded = false;
    private List<ShipCoords> coords;

    protected SlaversLoseState(VoyageState state, SlaversCard card, List<Player> list){
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<1||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
    }

    @Override
    public void init(ClientModelState new_state) {
        super.init(new_state);
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded&&!this.list.getFirst().getRetired()){
            this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
            return;
        }
        CrewRemoveVisitor v = new CrewRemoveVisitor(this.list.getFirst().getSpaceShip());
        for(ShipCoords s : this.coords) this.list.getFirst().getSpaceShip().getComponent(s).check(v);
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        return new ClientCrewPenaltyCardStateDecorator(
            new ClientBaseCardState(this.card.getId()), 
            this.list.getFirst().getColor(), 
            this.card.getCrewLost() - this.coords.size());
    }

    @Override
    protected CardState getNext(){
        if(this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()){
            this.list.removeFirst();
            if(!this.list.isEmpty()) return new SlaversAnnounceState(state, card, list);
            return null;
        }
        this.list.removeFirst();
        if(!list.isEmpty()) return new SlaversAnnounceState(state, card, list);
        return null;
    }

    @Override
    public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException{
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
        try {
            p.getSpaceShip().getComponent(cabin_coords).check(v);
        } catch(IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Sent coords of an empty cabin or not a cabin"));
            return;
        }
        if(p.getSpaceShip().getCrew()[0]==0){
            this.state.loseGame(p);
            this.validate(new EmptyMessage(p.getDescriptor()));
            return;
        }
        this.coords.add(cabin_coords);
        if(coords.size()==this.card.getCrewLost()){
            this.responded = true;
        }
    }

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.responded = true;
        }
        if(this.list.contains(p)){
            this.list.remove(p);
        }
    }
    
}
