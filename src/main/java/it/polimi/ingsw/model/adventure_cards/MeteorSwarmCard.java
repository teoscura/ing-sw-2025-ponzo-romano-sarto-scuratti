package it.polimi.ingsw.model.adventure_cards;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import it.polimi.ingsw.model.adventure_cards.exceptions.MismatchedProjectileTypes;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class MeteorSwarmCard extends Card{

    private Projectile[] meteorites;

    public MeteorSwarmCard(int id, Projectile[] meteorites){
        super(id, 0);
        if(meteorites==null || meteorites.length==0) throw new NullPointerException("Meteorites Array is empty or null");
        int max = 0;
        GameModeType type = meteorites[0].getType(); //Tutti i meteoriti devono essere dello stesso tipo.
        for(int i = 0; i<meteorites.length; i++){
            if(meteorites[i].getType()!=type) throw new MismatchedProjectileTypes("The provided projectile array has got different setups for its types.");
            if(meteorites[i].getOffset()!=-1) continue;
            if(meteorites[i].getDirection().getShift()-1%2==0) max = meteorites[i].getType().getHeight();
            else max = meteorites[i].getType().getWidth();
            int value = ThreadLocalRandom.current().nextInt(0, max);
            meteorites[i] = new Projectile(meteorites[i].getType(), meteorites[i].getDirection(), 
            meteorites[i].getDimension(), value);
        }
        this.meteorites = meteorites;
    }

    @Override
    public int apply(iSpaceShip ship, iPlayerResponse response){
        for(int i = 0; i<meteorites.length; i++){
            ship.handleMeteorite(meteorites[i]);
        }
        //TODO: create visitor for a in-line search and protection.
        return 0;
    }

}

