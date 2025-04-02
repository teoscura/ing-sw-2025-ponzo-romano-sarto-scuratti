package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.MeteorSwarmCard;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.player.PlayerColor;

public class MeteorNewCabinState extends CardState {

    private final MeteorSwarmCard card;
    private final List<PlayerColor> missing_cabin;

    public MeteorNewCabinState(ModelInstance model, MeteorSwarmCard card, List<PlayerColor> missing){
        super(model);
        if(missing==null || card==null) throw new NullPointerException();
        this.card = card;
        this.missing_cabin = missing;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }

    @Override
    public void validate(ServerMessage message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    protected CardState getNext() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNext'");
    }
    
}
