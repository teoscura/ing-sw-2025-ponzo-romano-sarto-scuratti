package it.polimi.ingsw.adventure_cards;

class Planet{
    int red_material;
    int blue_material;
    int green_material;
    int yellow_material;
    boolean is_visited;
}

public class PlanetCard {
    private int days_spent;
    private int level;
    private Planet[] planets;

    int delay_travel(){
        return days_spent;
    }
    void give_load_option(Planet planet, int player_id){}
}