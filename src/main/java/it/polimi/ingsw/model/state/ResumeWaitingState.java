package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class ResumeWaitingState extends GameState {
    
    private final GameState next;
    private final HashMap<String, ClientDescriptor> awaiting;
    private final PlayerCount count;
    private boolean done = false;

    public ResumeWaitingState(ModelInstance model, GameModeType type, PlayerCount count, GameState state) {
        super(model, type, count, null);
        if(state == null) throw new NullPointerException();
        this.next = state;
        this.awaiting = new HashMap<>();
        for(String s : this.next.players.stream().map(p->p.getUsername()).toList()){
            this.awaiting.put(s, null);
        }
        this.count = count;
    }

    @Override
    public void init(){
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!this.done){
            this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
            return;
        }
        for(Player p : this.players){
            p.bindDescriptor(this.awaiting.get(p.getUsername()));
        }
        this.transition();
    }

    @Override
    public GameState getNext() {
        return this.next;
    }

    @Override
    public ClientModelState getClientState(){
        ArrayList<ClientWaitingPlayer> tmp = new ArrayList<>();
        List<String> tmp2 = this.next.players.stream()
            .sorted((p1, p2) -> p1.getColor().getOrder()>p2.getColor().getOrder() ? 1 : -1)
            .map(p->p.getUsername()).toList();
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder() + 1 > this.count.getNumber()) break;
            if(c.getOrder()<0) continue;
            tmp.add(new ClientWaitingPlayer(tmp2.get(c.getOrder()), this.awaiting.get(tmp2.get(c.getOrder())) == null ? PlayerColor.NONE : c));
        }
        return new ClientWaitingRoomState(type, tmp);
    }

    @Override
    public boolean toSerialize() {
        return false;
    }

    public void connect(ClientDescriptor client) throws ForbiddenCallException {
        if(!this.awaiting.containsKey(client.getUsername())){
            System.out.println("Client '"+client.getUsername()+"' attempted to connect to a resuming game, but he wasn't playing in it before!");
            this.broadcastMessage(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect to a resuming game, but he wasn't playing in it before!"));
            return;
        }
        if(this.awaiting.get(client.getUsername())!=null){
            System.out.println("Client '"+client.getUsername()+"' attempted to connect to a resuming game, but someone already took that username's place!");
            this.broadcastMessage(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect to a resuming game, but someone already took that username's place!"));
            return;
        }
        this.awaiting.put(client.getUsername(), client);
        for(String username : this.next.players.stream().map(p->p.getUsername()).toList()){
            if(this.awaiting.get(username)==null) return;
        }
        this.done = true;
    }

    public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
        if(!this.awaiting.containsKey(client.getUsername())){
            System.out.println("Client '"+client.getUsername()+"' attempted to disconnect from a connection that isn't connected!");
            this.broadcastMessage(new ViewMessage("Client '"+client.getUsername()+"' attempted to disconnect from a connection that isn't connected!"));
            return;
        }
        if(this.awaiting.get(client.getUsername())==null){
            System.out.println("Client '"+client.getUsername()+"' attempted to disconnect from a connection that isn't connected!");
            this.broadcastMessage(new ViewMessage("Client '"+client.getUsername()+"' attempted to disconnect from a connection that isn't connected!"));
            return;
        }
        this.awaiting.put(client.getUsername(), null);
    }

    @Override
    public String toString() {
        return "should never be called";
    }

}
