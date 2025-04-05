package it.polimi.ingsw.model.state;

import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.message.client.DisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;

public class EndscreenState extends GameState {

    private final List<Player> awaiting;

    public EndscreenState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players) {
        super(model, type, count, players);
        this.awaiting = Arrays.asList(this.players);
    }

    @Override
    public void init(){
        for(Player p : this.players){
            if(p.getRetired()) continue;
            p.finalScore();
        }
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!awaiting.isEmpty()) return;
        this.transition();
    }

    @Override
    public GameState getNext() {
        return null;
    }

    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        if(!this.awaiting.contains(p)){
            p.getDescriptor().sendMessage(new DisconnectMessage());
            this.model.kick(p.getDescriptor());
            return;
        }
        this.awaiting.remove(p);
        p.getDescriptor().sendMessage(new DisconnectMessage());
        this.model.kick(p.getDescriptor());
        return;
    }

}
