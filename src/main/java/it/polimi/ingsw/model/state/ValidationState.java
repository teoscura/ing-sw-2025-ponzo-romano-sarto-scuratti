package it.polimi.ingsw.model.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;

public class ValidationState extends GameState {

    private final iCards voyage_deck;
    private final List<Player> validated;

    //XXX finish implementing

    public ValidationState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players, iCards voyage_deck) {
        super(model, type, count, players);
        if(voyage_deck==null) throw new NullPointerException();
        this.voyage_deck = voyage_deck;
        //TODO Auto-generated constructor stub
    }

    @Override
    public void init(){
        super.init();
        //XXX do a first verify run and send it to everyone.
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public GameState getNext() {
        Planche planche = new Planche(this.type, this.count/*,this.start_order */);
        return new VoyageState(model, type, count, players, voyage_deck, planche);
    }
    
    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }
    
    @Override
    public void removeComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    @Override
    public void setCrewType(Player p, ShipCoords coords, AlientType type) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }
    
}
