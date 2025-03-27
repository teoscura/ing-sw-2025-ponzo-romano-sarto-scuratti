package it.polimi.ingsw.controller.match;

import java.io.IOException;

import it.polimi.ingsw.controller.match.exceptions.GameAlreadyEndedException;
import it.polimi.ingsw.controller.match.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.match.state.GameState;
import it.polimi.ingsw.controller.match.state.WaitingRoomState;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.rulesets.GameModeType;
import it.polimi.ingsw.server.ClientDescriptor;
import it.polimi.ingsw.server.ServerController;

public class MatchController {
    private final ServerController server;
    private final long match_id;
    private boolean waiting = true;
    private boolean over = false;
    private GameState state;

    public MatchController(ServerController server, long match_id, GameModeType type, PlayerCount count, ClientDescriptor host){
        if(match_id<0) throw new NegativeArgumentException("Negative match id is forbidden");
        if(server == null || host == null) throw new NullPointerException();
        this.server = server;
        this.match_id = match_id;
        this.state = new WaitingRoomState(this, new ModelInstance(type, count), type, count, host);
    }

    public MatchController(ServerController server, long match_id, String path) throws IOException {
        if(match_id<0) throw new NegativeArgumentException("Negative match id is forbidden");
        if(server == null || path == null) throw new NullPointerException();
        this.server = server;
        this.match_id = match_id;
        //XXX All json loader logic.
    }

    public void setState(GameState state){
        this.state = state;
    }

    public void gameLoop(){
        while(true){
            if(this.state.finished()) this.state = this.state.getNext();
            this.state.action();
            //SEND everyone their spaceship state.
        }
    }

    public void startGame() throws GameAlreadyStartedException{
        if(!this.waiting) throw new GameAlreadyStartedException("Game is already ongoing.");
        this.waiting = false;
    }

    public void endGame() throws GameAlreadyEndedException{
        if(this.over) throw new GameAlreadyEndedException("Game is already finished.");
        this.over = true;;
    }

    public long getMatchId(){
        return this.match_id;
    }

    public synchronized boolean getWaiting(){
        return this.waiting;
    }

    public boolean getOver(){
        return this.over;
    }

    public void onClosure() throws IOException {
        //FIXME
        //XXX.
        //Serialize to JSON.
    }

}
