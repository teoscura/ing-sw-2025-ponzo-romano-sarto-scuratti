package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.*;

public class ModelInstance {
    
    private final PlayerCount count;
    private final iCommonBoard board;
    private final iSpaceShip[] ships;
    private final iPlanche planche;
    private final iCards[] card_piles;

    public ModelInstance(GameModeType type, PlayerCount count){
        this.count = count;
        this.board = new CommonBoard();
        this.ships = new iSpaceShip[count.getNumber()];
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder()>=ships.length) break;
            this.ships[c.getOrder()] = new SpaceShip(type, c);
        }
        this.planche = new Planche(type, count);
        //TODO load from factory.
        if(type.getLevel()==1){
            this.card_piles = new iCards[1];
            //LOAD FROM FACTORY.
            this.card_piles[0] = new Cards();
        }
        else{
            this.card_piles = new iCards[4];
            //TODO fill with different levels.
        }
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
