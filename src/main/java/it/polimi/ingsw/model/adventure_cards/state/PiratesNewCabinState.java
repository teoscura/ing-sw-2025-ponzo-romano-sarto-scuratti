package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PiratesCard;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class PiratesNewCabinState extends CardState {

    private final PiratesCard card;
    private final List<Player> list;

    //XXX implement accepted messages;

    public PiratesNewCabinState(VoyageState state, PiratesCard card, List<Player> list) {
        super(state);
        if(card==null||list==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        boolean missing = false;
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            missing = missing || p.getSpaceShip().getBrokeCenter();
        }
        if(missing) return;
        this.transition();
    }

    @Override
    protected CardState getNext() {
        this.list.removeFirst();
        if(!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
        return null;
    }
    
}
