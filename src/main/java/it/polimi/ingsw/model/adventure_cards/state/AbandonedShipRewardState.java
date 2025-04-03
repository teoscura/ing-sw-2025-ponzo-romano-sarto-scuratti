package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.AbandonedShipCard;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedShipRewardState extends CardState {
    
    private final AbandonedShipCard card;
    private final List<Player> list;
    private List<ShipCoords> coords;
    private boolean responded;

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
        this.list.getFirst().giveCredits(this.card.getCredits());
        this.state.getPlanche().movePlayer(this.list.getFirst().getColor(), -this.card.getDays());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return null;
    }

    @Override
    public void removeCrew(Player p, ShipCoords cabin_coords){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
        try {
            p.getSpaceShip().getComponent(cabin_coords).check(v);
        } catch(IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Sent coords of an empty cabin or not a cabin"));
            return;
        }
        this.coords.add(cabin_coords);
        if(coords.size()==this.card.getCrewLost()){
            this.responded = true;
        }
    }

}
