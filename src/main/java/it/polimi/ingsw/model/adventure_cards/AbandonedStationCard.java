package it.polimi.ingsw.model.adventure_cards;

public class AbandonedStationCard extends Card{
    int crew_required; //int (?) 
    int days_spent;
    int credits_gained;
    int red_material;
    int blue_material;
    int green_material;
    int yellow_material;

    void ask_to_explore(FirstPlayer){
        /*if yes and player.crew >= crew_required
            load resources//come planet card
            player.position -= days_spent;
        if no and player != last_player
            ask_to_expore(next_player);
        */
    }
}