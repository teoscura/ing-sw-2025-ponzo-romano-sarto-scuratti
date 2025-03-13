package it.polimi.ingsw.model.adventure_cards;

public class AbandonedShipCard extends Card{
    int crew_lost; //int (?) 
    int days_spent;
    int credits_gained;

    void ask_to_explore(FirstPlayer){
        /*if yes
            remove crew;
            player.credits += credits_gained;
            player.position -= days_spent;
        if no and player != last_player
            ask_to_expore(next_player);
        */
    }
}