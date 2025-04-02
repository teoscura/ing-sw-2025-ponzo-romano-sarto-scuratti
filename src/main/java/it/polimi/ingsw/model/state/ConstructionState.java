package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iCommonBoard;

public class ConstructionState extends GameState {
    
    private final iCommonBoard board;
    private final int[] construction_cards;
    private final iCards voyage_deck;

    public int[] getShowed(){
        return this.construction_cards;
    }

    public iCommonBoard getBoard(){
        return this.board;
    }

}
