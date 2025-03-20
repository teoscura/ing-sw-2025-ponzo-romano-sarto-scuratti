package it.polimi.ingsw.model.adventure_cards.events;

import it.polimi.ingsw.model.adventure_cards.Planet;

public class vAbandonedStationInfoEvent implements iCEvent{

    private Planet planet;
    private int crew;

    public vAbandonedStationInfoEvent(Planet planet, int crew){
        this.planet = planet;
        this.crew = crew;
    }

}
