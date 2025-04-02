package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.AbandonedShipCard;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedShipRewardState extends CardState {
    
    private final AbandonedShipCard card;
    private final List<Player> list;
    private List<ShipCoords> coords;
    private List<ShipmentType> merch;
    private boolean responded;

    //XXX implement allowed messages.

    public AbandonedShipRewardState(VoyageState state, AbandonedShipCard card, List<Player> list) {
        super(state);
        if(state==null||list==null||card==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        this.list = list;
        this.card = card;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        for(int i=0;i<this.coords.size();i++){
            ((StorageComponent) this.list.getFirst().getSpaceShip().getComponent(this.coords.get(i))).putIn(this.merch.get(i));
        }
        this.state.getPlanche().movePlayer(this.list.getFirst().getColor(), -this.card.getDays());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return null;
    }
}
