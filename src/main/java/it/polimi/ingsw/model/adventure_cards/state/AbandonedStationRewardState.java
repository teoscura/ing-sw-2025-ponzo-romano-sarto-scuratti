package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.PlanetCard;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import it.polimi.ingsw.model.player.Player;

public class AbandonedStationRewardState extends CardState {

    private final Planet reward;
    private final Player target;

    public AbandonedStationRewardState(ModelInstance model, Player target, Planet planet) {
        super(model);
        if(target.getColor().getOrder()>this.model.getCount().getNumber()||target==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(planet==null) throw new NullPointerException();
        this.target = target;
        this.reward = planet;
    }

    @Override
    public void init() {
        this.model.getPlayer(target.getColor()).getDescriptor().sendMessage(new CargoMessage(this.reward.getContains()));
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        
    }

    @Override
    protected CardState getNext() {
        return null;
    }
}
