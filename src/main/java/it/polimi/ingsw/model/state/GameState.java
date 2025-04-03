package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public abstract class GameState {
    
    protected final ModelInstance model;
    protected final GameModeType type;
    protected final PlayerCount count;
    protected final Player[] players;

    public GameState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players){
        if(model==null) throw new NullPointerException();
        if(players!=null&&players.length!=count.getNumber()) throw new IllegalArgumentException("Illegal GameState created");
        this.model = model;
        this.type = type;
        this.count = count;
        this.players = players;
    }

    public void init(){
        for(Player p : this.players){
            p.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
        }
    }

    public abstract void validate(ServerMessage message) throws ForbiddenCallException;
    public abstract GameState getNext();
    //TODO public abstract JsonState serialize();

    //TODO public abstract ClientState getClientState();

    public void transition(){
        this.model.setState(this.getNext());
    }

    public PlayerCount getCount(){
        return this.count;
    }

    public Player getPlayer(PlayerColor c) throws PlayerNotFoundException {
        if(c.getOrder()>=players.length) throw new PlayerNotFoundException("Player color is not present in this match");
        if(this.players[c.getOrder()].getRetired()) throw new PlayerNotFoundException("Player has lost or retired from the game");
        return this.players[c.getOrder()];
    }

    public GameModeType getType(){
        return this.type;
    }

    //Methods that can be called and overridden by subclasses.
    public void connect(ClientDescriptor client) throws ForbiddenCallException {
        client.sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void sendContinue(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void putComponent(Player p, ShipCoords coords, int id) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void takeComponent(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void discardComponent(Player p, int id) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void toggleHourglass(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void removeComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void setCrewType(Player p, ShipCoords coords, AlientType type) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void giveUp(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This operation isn't allowed in the current state!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public CardState getCardState(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("Current state isn't Voyage!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

}
