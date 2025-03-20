package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CombatZoneCard extends Card{
    // int days_lost;
    // int crew_lost;
    // ArrayList<Shot> shots = new ArrayList<Shot>();

    public CombatZoneCard(int id, int days){
        super(id, days);
        if(days<=0) throw new IllegalArgumentException("Negative arguments not allowed.");
        //TODO
    }

    @Override
    public iCEvent setup(iSpaceShip state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setup'");
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }

    
    
    /*for(players ordered ){
        //ask to activate cannons and engines
    }
    player_smallest_crew = player.findPlayer();
    player_smallest_crew.position -= days_lost;
    player_lowest_engines = player.findPlayer();
    //ask player_lowest_engines to remove two crew members
    player_lowest_cannons = player.findPlayer();
    //tiro_dadi
    //cords = shipCords findComponentHit(player_lowest_cannons, tiro_dadi, shots[i].direction);
    //for every shot{
        void hit(shipCords){
            if shipCords == (0,0) missed;
            getComponent(shipCords);
            if (shot.size == SMALL && shield active){
                deflected
            }
            else component_destroyed();
        }
    }*/
    
}
