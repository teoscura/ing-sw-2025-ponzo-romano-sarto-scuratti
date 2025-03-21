package it.polimi.ingsw.model.adventure_cards;

import java.util.concurrent.ThreadLocalRandom;

import it.polimi.ingsw.model.adventure_cards.exceptions.MismatchedProjectileTypes;
import it.polimi.ingsw.model.adventure_cards.utils.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class CombatZoneCard extends Card{
    
    private Projectile[] shots;
    private int crew;

    public CombatZoneCard(int id, int days, int crew, Projectile[] shots){
        super(id, days);
        if(shots==null || shots.length==0) throw new NullPointerException("Shots array is empty or null");
        int max = 12;
        GameModeType type = shots[0].getType(); //Tutti i proiettili devono essere dello stesso tipo di gamemode.
        for(int i = 0; i<shots.length; i++){
            if(shots[i].getType()!=type) throw new MismatchedProjectileTypes("The provided projectile array has got different setups for its types.");
            if(shots[i].getOffset()!=-1) continue;
            int value = ThreadLocalRandom.current().nextInt(1, max+1);
            shots[i] = new Projectile(shots[i].getType(), 
                                      shots[i].getDirection(), 
                                      shots[i].getDimension(), 
                                      value);
        }
        this.shots = shots;
    }

    // WRONG: 0 fastest, 1 smallest crew, 2 lowest engines, 3 lowest cannons.
    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null || response == null) throw new NullPointerException();
        //TODO: rework with variable conditions.
        return new DaysCardResponse(0);
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
