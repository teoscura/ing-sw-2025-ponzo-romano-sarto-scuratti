package it.polimi.ingsw.model.cards;

import java.util.ArrayList;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.MeteorAnnounceState;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class MeteorSwarmCard extends Card {

    private final ProjectileArray meteorites;

    public MeteorSwarmCard(int id, ProjectileArray meteorites){
        super(id, 0);
        this.meteorites = meteorites;
    }

    @Override
    public CardState getState(VoyageState state) {
        return new MeteorAnnounceState(state, this.getId(), meteorites);
    }

    public ArrayList<Projectile> getMeteorites(){
        return this.meteorites.getProjectiles();
    }

    public boolean apply(Player p, Projectile meteorite){
        if(p==null) throw new NullPointerException();
        return p.getSpaceShip().handleMeteorite(meteorite);
    }

}

