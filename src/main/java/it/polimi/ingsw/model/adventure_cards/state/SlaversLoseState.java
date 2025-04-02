package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SlaversCard;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class SlaversLoseState extends CardState {

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
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        CrewRemoveVisitor v = new CrewRemoveVisitor(this.list.getFirst().getSpaceShip());
        for(ShipCoords s : this.coords) this.list.getFirst().getSpaceShip().getComponent(s).check(v);
        this.transition();
    }

    @Override
    protected CardState getNext(){
        this.list.removeFirst();
        if(!list.isEmpty()) return new SlaversAnnounceState(state, card, list);
        return null;
    }
    
}
