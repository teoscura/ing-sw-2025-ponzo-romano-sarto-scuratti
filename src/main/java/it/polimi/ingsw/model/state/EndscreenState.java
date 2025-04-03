package it.polimi.ingsw.model.state;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;

public class EndscreenState extends GameState {

    //XXX finish implementing

    public EndscreenState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players) {
        super(model, type, count, players);
    }

    @Override
    public void init(){
        super.init();
        //XXX calculate scores for everyone in a formatted way.
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public GameState getNext() {
        return null;
    }

    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

}
