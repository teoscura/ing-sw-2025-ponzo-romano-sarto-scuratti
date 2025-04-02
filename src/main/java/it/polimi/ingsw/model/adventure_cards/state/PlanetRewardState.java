package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.CargoMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.PlanetCard;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class PlanetRewardState extends CardState {

    private final PlanetCard card;
    private final List<Player> list;
    
    public PlanetRewardState(VoyageState state, PlanetCard card, List<Player> clist) {
        super(state);
        if(clist.size()>this.state.getCount().getNumber()||clist.size()<2||clist==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.list = clist;
        this.card = card;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) asdasd
    }
    
}
