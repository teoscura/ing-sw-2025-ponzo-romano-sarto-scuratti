package it.polimi.ingsw.model.adventure_cards.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.AbandonedStationCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedStationAnnounceState extends CardState {

    private final AbandonedStationCard card;
    private final List<Player> list;
    private boolean responded = false;
    private int id = -1;

    public AbandonedStationAnnounceState(VoyageState state, AbandonedStationCard card, List<Player> list) {
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<1||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
    }

    @Override
    public void init(ClientModelState new_state) {
        super.init(new_state);
        if(list.getFirst().getRetired()||list.getFirst().getDisconnected()) this.transition();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded){
            this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
            return;
        }
        this.card.apply(state, this.list.getFirst(), id);
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        ArrayList<Boolean> tmp = new ArrayList<>(Arrays.asList(new Boolean[]{true}));
        return new ClientLandingCardStateDecorator(new ClientBaseCardState(this.id), 
                                                   this.list.getFirst().getColor(), 
                                                   this.card.getDays(), 
                                                   this.card.getCrewLost(), 
                                                   tmp);
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return new AbandonedStationRewardState(state, card, list);
        this.list.removeFirst();
        if(!this.list.isEmpty()) return new AbandonedStationAnnounceState(state, card, list);
        return null;
    }

    @Override
    public void selectLanding(Player p, int planet){
        if(p!=this.list.getFirst()){
            System.out.println("Player '"+p.getUsername()+"' attempted to land during another player's turn!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to land during another player's turn!"));
            return;
        }
        else if(planet!=-1 && planet!=0){ 
            System.out.println("Player '"+p.getUsername()+"' attempted to land on an invalid id!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to land on an invalid id!"));
            return;
        }
        this.id = planet;
        this.responded = true;
    }

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.responded = true;
            this.id = -1;
            return;
        }
        if(this.list.contains(p)){
            this.list.remove(p);
        }
    }
    
}
