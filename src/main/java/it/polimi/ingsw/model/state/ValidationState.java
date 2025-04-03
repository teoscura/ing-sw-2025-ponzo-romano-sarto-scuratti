package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.player.Player;

public class ValidationState extends GameState {

    private final iCards voyage_deck;
    //private List<PlayerColor> start_order;

    //XXX finish implementing

    public ValidationState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players, iCards voyage_deck) {
        super(model, type, count, players);
        if(voyage_deck==null) throw new NullPointerException();
        this.voyage_deck = voyage_deck;
        //TODO Auto-generated constructor stub
    }

    @Override
    public GameState getNext() {
        // crea planche con ordine;
        // restituisci deck di carte;
        Planche planche = new Planche(this.type, this.count/*,this.start_order */);
        return new VoyageState(model, type, count, players, voyage_deck, planche);
    }
    
}
