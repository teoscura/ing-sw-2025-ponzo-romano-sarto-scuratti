package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.CargoMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.AbandonedStationCard;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedStationRewardState extends CardState {

    private final AbandonedStationCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean took_reward = false;
    private List<ShipCoords> coords = null;
    private List<ShipmentType> merch = null;

    //XXX implement allowed messages.

    public AbandonedStationRewardState(VoyageState state, AbandonedStationCard card, List<Player> list) {
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<1||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
    }

    @Override
    public void init() {
        super.init();
        try {
            this.state.getPlayer(list.getFirst().getColor()).getDescriptor().sendMessage(new CargoMessage(this.card.getPlanet().getContains()));
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        if(took_reward){
            for(int i=0;i<this.coords.size();i++){
                ((StorageComponent) this.list.getFirst().getSpaceShip().getComponent(this.coords.get(i))).putIn(this.merch.get(i));
            }
        }
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return null;
    }
}
