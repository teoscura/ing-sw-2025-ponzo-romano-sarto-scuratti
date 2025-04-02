package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SmugglersCard;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class SmugglersLoseState extends CardState {

    private final SmugglersCard card;
    private final List<Player> list;
    private boolean responded = false;
    private List<ShipCoords> coords = null;

    //XXX implement accepted messages;

    public SmugglersLoseState(VoyageState state, SmugglersCard card, List<Player> list){
        super(state);
        if(state==null||card==null||list==null||list.size()>this.state.getCount().getNumber()||list.size()<1) throw new IllegalArgumentException("Created unsatisfyable state");
        this.card = card;
        this.list = list;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        ContainsRemoveVisitor v = new ContainsRemoveVisitor();
        for(ShipCoords s : this.coords) this.list.getFirst().getSpaceShip().getComponent(s).check(v);
        this.transition();
    }

    @Override
    protected CardState getNext() {
        this.list.removeFirst();
        if(!list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
        return null;
    }
    
}
