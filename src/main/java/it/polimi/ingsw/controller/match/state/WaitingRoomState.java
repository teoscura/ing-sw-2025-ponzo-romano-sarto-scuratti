package it.polimi.ingsw.controller.match.state;


import java.util.ArrayList;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.controller.match.PlayerCount;
import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.player.PlayerDisconnectMessage;
import it.polimi.ingsw.message.player.PlayerJoinMessage;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class WaitingRoomState extends GameState {

    private final ClientDescriptor[] players;

    public WaitingRoomState(MatchController match, 
                            GameModeType type, 
                            PlayerCount count,
                            ClientDescriptor host){
        super(match, type);
        this.players = new ClientDescriptor[count.getNumber()];
        this.players[0] = host;
        match.sendMessage(/*XXX dire al giocatore la waiting room e il suo slot.*/)
    }

    public void onMessage(Message m) {
        
    }

    public void onMessage(PlayerJoinMessage m) {
        
    }

    public void onMessage(PlayerDisconnectMessage m) {
        
    }
    
}
