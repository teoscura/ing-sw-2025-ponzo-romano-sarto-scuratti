package it.polimi.ingsw.model.state;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

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
        //TODO send everyone notify to change gamestate;
    }

    public GameModeType getType(){
        return this.type;
    }

    public PlayerCount getCount(){
        return this.count;
    }

    public Player getPlayer(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=players.length) throw new PlayerNotFoundException("Player color is not present in this match");
        if(this.players[c.getOrder()].getRetired()) throw new PlayerNotFoundException("Player has lost or retired from the game");
        return this.players[c.getOrder()];
    }

    protected void setCardState(CardState state) throws ForbiddenCallException{
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    public void transition(){
        this.model.setState(this.getNext());
    }

    public abstract GameState getNext();

    //TODO public abstract ClientState getClientState();

    //TODO public abstract JsonState serialize();

}
