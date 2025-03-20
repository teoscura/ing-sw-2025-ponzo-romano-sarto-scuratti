package it.polimi.ingsw.model.adventure_cards.events;

import it.polimi.ingsw.model.adventure_cards.Planet;

public class vPlanetInfoEvent implements iCEvent {
    
    private Planet[] planets;

    public vPlanetInfoEvent(Planet[] planets){
        this.planets = planets; //TODO checks.
    }

}
