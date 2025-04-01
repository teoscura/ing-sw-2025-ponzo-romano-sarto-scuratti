package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.MeteorAnnounceState;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;

public class MeteorSwarmCard extends Card{

    private final ProjectileArray meteorites;

    public MeteorSwarmCard(int id, ProjectileArray meteorites){
        super(id, 0);
        this.meteorites = meteorites;
    }

    @Override
    public CardState getState(ModelInstance model) {
        return new MeteorAnnounceState(model, this, meteorites);
    }

    public Projectile[] getMeteorites(){
        return this.meteorites.getProjectiles().clone();
    }

    public boolean apply(Player p, Projectile meteorite){
        if(p==null) throw new NullPointerException();
        return p.getSpaceShip().handleMeteorite(meteorite);
    }

}

