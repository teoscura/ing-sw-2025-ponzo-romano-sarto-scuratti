package it.polimi.ingsw.controller.match;

import java.io.IOException;

import it.polimi.ingsw.controller.match.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.match.state.GameState;
import it.polimi.ingsw.controller.match.state.WaitingRoomState;
import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.controller.message.server.ServerMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class MatchController {
    private final ServerController server;
    private final long match_id;
    private boolean waiting = true;
    private GameState state;

    public MatchController(ServerController server, long match_id, GameModeType type, PlayerCount count, ClientDescriptor host){
        if(match_id<0) throw new NegativeArgumentException("Negative match id is forbidden");
        if(server == null || host == null) throw new NullPointerException();
        this.server = server;
        this.match_id = match_id;
        this.state = new WaitingRoomState(this, type, count, host);
    }

    public MatchController(ServerController server, long match_id, String path) throws IOException {
        if(match_id<0) throw new NegativeArgumentException("Negative match id is forbidden");
        if(server == null || path == null) throw new NullPointerException();
        this.server = server;
        this.match_id = match_id;
        //XXX All json loader logic.
    }

    public void sendMessage(ServerMessage m){
        this.server.sendMessage(m);
    }

    public synchronized void recieveMessage(PlayerColor color, Message message){
        message.sendTo(this.state);
    }

    public void setState(GameState state){
        this.state = state;
    }

    public void startGame() throws GameAlreadyStartedException{
        if(!this.waiting) throw new GameAlreadyStartedException("Game is already ongoing.");
    }

    public long getMatchId(){
        return this.match_id;
    }

    public synchronized boolean getWaiting(){
        return this.waiting;
    }

    public void onClosure() throws IOException {
        //FIXME
        //XXX.
        //Serialize to JSON.
    }

}
