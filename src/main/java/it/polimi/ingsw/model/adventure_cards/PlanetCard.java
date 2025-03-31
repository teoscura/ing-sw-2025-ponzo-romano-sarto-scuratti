//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.model.adventure_cards.responses.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.PlanetCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;

public class PlanetCard extends Card {
		
	private final Planet[] planets;
	private int left;

	public PlanetCard(int id, int days, Planet[] planets) { // costruttore
		super(id, days);
		this.planets = planets;
		this.left = planets.length;
	}

	@Override
	public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
		if(ship==null || response == null) throw new NullPointerException();
		if(response.getId()>=this.planets.length) throw new ArgumentTooBigException( "Sent a planet id larger than the list.");
		if(response.getId()==-1) return new DaysCardResponse(0);
		this.planets[response.getId()].visit();
		this.left-=1;
		if(this.left==0) this.exhaust();
		return new PlanetCardResponse(this.planets[response.getId()], this.days);
	}

}