package it.polimi.ingsw.model.state;

import java.util.ArrayList;
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
    private final List<Player> order_arrival;

    public EndscreenState(ModelInstance model, GameModeType type, PlayerCount count, List<Player> players, List<Player> order_arrival) {
        super(model, type, count, players);
        if(order_arrival==null) throw new NullPointerException();
        this.order_arrival = order_arrival;
        this.awaiting = new ArrayList<>();
        this.awaiting.addAll(this.players);
    }

    @Override
    public void init(){
        int min = Integer.MAX_VALUE;
        for(Player p : this.players){
            if(p.getRetired()) continue;
            min = p.getSpaceShip().countExposedConnectors() <= min ? p.getSpaceShip().countExposedConnectors() : min;
        }
        for(Player p : this.players){
            if(p.getRetired()) continue;
            if(p.getSpaceShip().countExposedConnectors()==min) p.addScore(2);
            if(this.order_arrival.contains(p)) p.addScore(4-order_arrival.indexOf(p));
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
