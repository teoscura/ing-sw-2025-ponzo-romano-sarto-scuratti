package it.polimi.ingsw.model.adventure_cards;

public class SmugglersCard extends Card{
    int cannon_power_required;
    int red_material;
    int blue_material;
    int green_material;
    int yellow_material;
    int days_spent;
    int good_lost; //(take your n most valuable goods. If you run out of goods, they take batteries instead.) 

    void fight(/*first_player*/){
        /*if player.power > cannon_power_required{
            ask_to_load
            if yes{
                loose days
                load goods
            }
        else if player.power = cannon_power_required{
            fight(next_player)
            }
        else if player.power < cannon_power_required{
            lose_merch() //takes n most precious resources, if not enough resources, steals batteries
            }
        }
    */
    }
}