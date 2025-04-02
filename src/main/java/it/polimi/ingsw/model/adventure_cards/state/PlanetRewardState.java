package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PlanetCard;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class PlanetRewardState extends CardState {

    private final PlanetCard card; //Used for validation
    private final List<Player> list;
    private final int id; //Used for validation of 
    private boolean responded = false;
    private List<ShipCoords> coords = null;
    private List<ShipmentType> merch = null;
    
    //XXX implement accepted messages;

    public PlanetRewardState(VoyageState state, PlanetCard card, int id, List<Player> clist) {
        super(state);
        if(clist.size()>this.state.getCount().getNumber()||clist.size()<1||clist==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.id = id;
        this.list = clist;
        this.card = card;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded && !this.list.getFirst().getDisconnected()) return;
        for(int i=0;i<this.coords.size();i++){
            ((StorageComponent) this.list.getFirst().getSpaceShip().getComponent(this.coords.get(i))).putIn(this.merch.get(i));
        }
        this.state.getPlanche().movePlayer(this.list.getFirst().getColor(), -this.card.getDays());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return null;
        this.list.removeFirst();
        if(this.list.isEmpty()) return new PlanetAnnounceState(state, card, list);
        return null;
    }
    
}
