package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SlaversCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class SlaversAnnounceState extends CardState {

    private final SlaversCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean result = false;

    public SlaversAnnounceState(VoyageState state, SlaversCard card, List<Player> list){
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<1||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
    }

    @Override
    public void init() {
        super.init();
        if(list.getFirst().getRetired()||list.getFirst().getDisconnected()) this.transition();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        this.result = this.card.apply(state, this.list.getFirst());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(list.getFirst().getRetired()||list.getFirst().getDisconnected()){
            this.list.removeFirst();
            if(!list.isEmpty()) return new SlaversAnnounceState(state, card, list);
        };
        if(this.card.getExhausted()) return new SlaversRewardState(state, card, list);
        if(!result && !this.list.getFirst().getRetired()) return new SlaversLoseState(state, card, list);
        this.list.removeFirst();
        if(!list.isEmpty()) return new SlaversAnnounceState(state, card, list);
        return null;
    }

    @Override
    public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
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
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else"));
            return;
        }
        this.responded = true;
    }

    
}
