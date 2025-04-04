//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.adventure_cards.state.AbandonedStationAnnounceState;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedStationCard extends Card{
    
    private Planet planet;
    private int crew;

    public AbandonedStationCard(int id, int days, Planet planet, int crew){
        super(id, days);
        if(crew<=0) throw new NegativeArgumentException("Crew required can't be negative.");
        if(planet==null) throw new NullPointerException();
        this.crew=crew;
        this.planet=planet;
    }

    @Override
    public CardState getState(VoyageState state){
        return new AbandonedStationAnnounceState(state, this, state.getOrder(CardOrder.NORMAL));
    }

    public Planet getPlanet(){
        return this.planet;
    }

    public int getCrewLost(){
        return this.crew;
    }

    public void apply(VoyageState state, Player p, int id){
        if(state==null||p==null) throw new NullPointerException();
        if(id==0){
            if(p.getSpaceShip().getTotalCrew()<this.crew) throw new CrewSizeException("Crew too small to salvage station.");
            this.exhaust();
            state.getPlanche().movePlayer(p, -this.days);
        }
    }

}