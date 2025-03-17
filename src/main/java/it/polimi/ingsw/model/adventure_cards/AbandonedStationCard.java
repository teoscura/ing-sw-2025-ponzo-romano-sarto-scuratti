package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.player.PlayerColor;

public class AbandonedStationCard extends Card{
    int crew_required; //int (?) 
    int days_spent;
    int credits_gained;
    int red_material;
    int blue_material;
    int green_material;
    int yellow_material;

    public void askToExploreStation(PlayerColor current_player/* planche.getFirstPlayer() */){
        /*if yes and player.crew >= crew_required
            load resources//come planet card
            player.position -= days_spent;
        if no and player != last_player

        */
            askToExploreStation(Planche.getNextPlayer(current_player));

    }
}