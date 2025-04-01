package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.PlanetCard;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.player.Player;

public class PlanetRewardState extends CardState {

    private final PlanetCard card;
    private final List<Player> list;
    
    public PlanetRewardState(ModelInstance model, PlanetCard card, List<Player> clist) {
        super(model);
        if(clist.size()>this.model.getCount().getNumber()||clist.size()<2||clist==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.list = clist;
        this.card = card;
    }

    @Override
    public void init() {
        this.model.getPlayer(list.getFirst().getColor()).getDescriptor().sendMessage(new CargoMessage(this.card.getPlanet().getContains()));
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) asdasd
    }
    
}
