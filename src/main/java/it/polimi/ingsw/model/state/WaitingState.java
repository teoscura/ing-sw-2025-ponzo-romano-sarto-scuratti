package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.client.ClientDisconnectMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
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
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(this.connected.size()<count.getNumber()){
            this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
            return;
        }
        this.transition();
    }

    @Override
    public GameState getNext() {
        List<Player> playerlist = new ArrayList<>();
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder()<0) continue;
            String username = this.connected.get(c.getOrder()).getUsername();
            playerlist.addLast(new Player(this.type, username, c));
            try {
                this.connected.get(c.getOrder()).bindPlayer(playerlist.get(c.getOrder()));
            } catch (Exception e) {
                //Unreachable, but needed by compiler.
                e.printStackTrace();
            }
        }
        return new ConstructionState(model, type, count, playerlist);
    }

    @Override
    public ClientModelState getClientState(){
        ArrayList<ClientWaitingPlayer> tmp = new ArrayList<>();
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder() + 1 > this.count.getNumber()) break;
            if(c.getOrder()<0) continue;
            tmp.add(new ClientWaitingPlayer(this.connected.get(c.getOrder()).getUsername(), c));
        }
        return new ClientWaitingRoomState(type, tmp);
    }

    public void connect(ClientDescriptor client) throws ForbiddenCallException {
        if(this.connected.contains(client)){
            System.out.println("Client '"+client.getUsername()+"' attempted to connect from an already connected connection!");
            this.broadcastMessage(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect from an already connected connection!"));
            return;
        }
        this.connected.add(client);
    }

    public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
        if(!this.connected.contains(client)){
            System.out.println("Client '"+client.getUsername()+"' attempted to disconnect from a connection that isn't connected!");
            this.broadcastMessage(new ViewMessage("Client '"+client.getUsername()+"' attempted to disconnect from a connection that isn't connected!"));
            return;
        }
        this.connected.remove(client);
    }
    

}
