package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.match.PlayerCount;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iCommonBoard;
import it.polimi.ingsw.model.board.iPlanche;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.iSpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class ModelInstance {
    
    private final PlayerCount count;
    private final iCommonBoard board;
    private final iSpaceShip[] ships;
    private final iPlanche planche;
    private final iCards[] card_piles;

    public ModelInstance(GameModeType type, PlayerCount count){
        //TODO LOGIC
        this.count = count;
        this.board = new CommonBoard();
    }

    public PlayerCount getCount(){
        return this.count;
    }

    public iCommonBoard getBoard(){
        return this.board;
    }

    public iSpaceShip getPlayer(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=ships.length) throw new PlayerNotFoundException("Player color is not present in this match");
        return this.ships[c.getOrder()];
    }

    public iPlanche getPlanche(){
        return planche;
    }

    public iCard pickCard(int i){
        //TODO
        return null;
    }
}
