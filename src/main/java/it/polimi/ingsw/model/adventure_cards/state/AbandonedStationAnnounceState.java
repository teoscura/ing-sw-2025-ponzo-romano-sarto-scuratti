package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.AbandonedStationCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
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
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        this.card.apply(state, this.list.getFirst(), id);
        this.transition();
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
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        else if(planet!=-1 && planet!=0){ 
            p.getDescriptor().sendMessage(new ViewMessage("Wrong landing id sent!"));
            return;
        }
        this.id = planet;
        this.responded = true;
    }
    
}
