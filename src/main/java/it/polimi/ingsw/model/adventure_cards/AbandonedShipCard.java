package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.player.PlayerColor;

public class AbandonedShipCard extends Card{
    int crew_lost; //int (?) 
    int days_spent;
    int credits_gained;

    public void askToExploreShip(PlayerColor current_player/* planche.getFirstPlayer() */){
        /*if yes
            remove crew;
            player.credits += credits_gained;
            player.position -= days_spent;
        if no and player != last_player
        */
            askToExploreShip(Planche.getNextPlayer(current_player));
        
    }
}