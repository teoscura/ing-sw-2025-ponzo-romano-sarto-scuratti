package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public class MeteorSwarmCard extends Card{

    //TODO: direzioni.
    private Projectile[] meteorites;

    public MeteorSwarmCard(Projectile[] meteorites, int id){
        super(id);
        this.meteorites = meteorites;
    }



    @Override
    public int apply(iSpaceShip ship, iPlayerResponse response){
        //TODO capire come far vedere all'utente le lane in cui l'abbiamo randomizzata.
        for(Projectile p : meteorites){
            ship.handleMeteorite(p);
        }
        //TODO: create visitor for a in-line search and protection.
        return 0;
    }
}

