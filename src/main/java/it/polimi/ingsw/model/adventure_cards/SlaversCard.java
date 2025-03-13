package it.polimi.ingsw.model.adventure_cards;

public class SlaversCard extends Card{
    int cannon_power_needed;
    int crew_lost;
    int days_spent;
    int coins_earned;

    /*void fight(first player){
        if (player.cannon_power > cannon_power_needed){
            askForReward
            if yes{
                player.coins += coins_earned;
                player.position -= days_spent
            }
        }
        else if (player.cannon_power = cannon_power_needed){
            fight(next_player());
        }
        else if (player.cannon_power < cannon_power_needed){
            remove_crew();
            fight(next_player());
        }
    }*/
}
