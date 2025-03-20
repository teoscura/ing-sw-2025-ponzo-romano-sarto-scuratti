package it.polimi.ingsw.model.adventure_cards;
import java.util.ArrayList;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.iSpaceShip;


public class PiratesCard extends Card{
    // int cannon_power_needed;
    // int coins_earned;
    // int days_spent;
    // ArrayList<Shot> shots = new ArrayList<Shot>();
    // ArrayList<PlayerColor> players_defeated = new ArrayList<PlayerColor>();

    public PiratesCard(int id, int days){
        super(id, days);
        //TODO
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }

    


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
