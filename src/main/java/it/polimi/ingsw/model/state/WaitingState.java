package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class WaitingState extends GameState {

    private final List<ClientDescriptor> connected;
    private final PlayerCount count;

    public WaitingState(ModelInstance model, GameModeType type, PlayerCount count) {
        super(model, type, count, null);
        this.connected = new ArrayList<>();
        this.count = count;
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(this.connected.size()<count.getNumber()) return;

        this.transition();
    }

    @Override
    public GameState getNext() {
        List<Player> playerlist = new ArrayList<>();
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder()<0) continue;
            playerlist.addLast(new Player(this.type, c));
            try {
                this.connected.get(c.getOrder()).bindPlayer(playerlist.get(c.getOrder()));
            } catch (Exception e) {
                //Unreachable, but needed by compiler.
                e.printStackTrace();
            }
        }
        return new ConstructionState(model, type, count, playerlist);
    }

    public void connect(ClientDescriptor client) throws ForbiddenCallException {
        if(this.connected.contains(client)){
            client.sendMessage(new ViewMessage("You cannot connect twice!"));
            return;
        }
        this.connected.add(client);
        for(ClientDescriptor broadcast : this.connected){
            broadcast.sendMessage(new NotifyStateUpdateMessage());
        }
    }

    public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
        if(!this.connected.contains(client)){
            client.sendMessage(new ViewMessage("You cannot disconnect twice!"));
            return;
        }
        this.connected.remove(client);
        for(ClientDescriptor broadcast : this.connected){
            broadcast.sendMessage(new NotifyStateUpdateMessage());
        }
    }
    

}
