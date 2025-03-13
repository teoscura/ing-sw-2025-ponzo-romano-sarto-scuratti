package it.polimi.ingsw.model.adventure_cards;
import java.util.ArrayList;

import it.polimi.ingsw.model.player.Player;

public class PlanetCard extends Card {
    public PlanetCard(int id){ //costruttore
      
    }
    ArrayList<Planet> planets = new ArrayList<Planet>();
    int days_spent; 

    void visit(PlayerList){ 
        /*for Player in PlayerList{
            chiedi di atterrare
            if yes 
                load resources  // load goods on ship
                player.position -= days_spent
        }*/
        for (int i=0; i< Playerlist.size(); i++){
            if(chiedi di atterrare)
                //load resources
                player.position -= days_spent
        }
    }
}