package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public class SlaversCard extends Card{
    // int cannon_power_needed;
    // int crew_lost;
    // int days_spent;
    // int coins_earned;

    public SlaversCard(int id){
        super(id);
        //TODO
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }

    //public void fightSlavers(PlayerColor current_player/* planche.getFirstPlayer() */){
    
    /*    if (player.cannon_power > cannon_power_needed){
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

