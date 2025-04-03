package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.ClientPlayer;
import it.polimi.ingsw.model.player.PlayerColor;

public class VoyageState extends ClientState {
    
    private final List<ClientPlayer> players;
    private final PlayerColor turn;
    private final int game_turn;
    private final int card_id;

}
