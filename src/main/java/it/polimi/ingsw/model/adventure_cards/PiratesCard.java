package it.polimi.ingsw.model.adventure_cards;
import java.util.ArrayList;


public class PiratesCard extends Card{
    int cannon_power_needed;
    int coins_earned;
    int days_spent;
    ArrayList<Shot> shots = new ArrayList<Shot>();
    ArrayList<Player> players_defeated = new ArrayList<Player>();

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
            add player to players_defeated;
            fight(next_player());
        }
    }
    for every player in players defeated{
        //for every shot{
            /*void hit(shipCords){
                if shipCords == (0,0) missed;
                getComponent(shipCords);
                if (shot.size == SMALL && shield active){
                    deflected
                }
                else component_destroyed();
            }
        }
    }*/
    
}
