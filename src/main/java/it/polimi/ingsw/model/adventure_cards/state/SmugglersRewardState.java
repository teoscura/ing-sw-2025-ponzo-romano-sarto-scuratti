package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SmugglersCard;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class SmugglersRewardState extends CardState {

    private final SmugglersCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean took_reward = false;
    private List<ShipCoords> coords = null;
    private List<ShipmentType> merch = null;

    public SmugglersRewardState(VoyageState state, SmugglersCard card, List<Player> list){
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
        if(took_reward){
            for(int i=0;i<this.coords.size();i++){
                ((StorageComponent) this.list.getFirst().getSpaceShip().getComponent(this.coords.get(i))).putIn(this.merch.get(i));
            }
            this.state.getPlanche().movePlayer(this.list.getFirst().getColor(), -this.card.getDays());
        }
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return null;
    }
    
}
