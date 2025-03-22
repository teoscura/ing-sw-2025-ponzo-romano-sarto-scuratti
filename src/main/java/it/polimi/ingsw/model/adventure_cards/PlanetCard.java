//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.model.adventure_cards.utils.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import it.polimi.ingsw.model.adventure_cards.utils.PlanetCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;

public class PlanetCard extends Card {
		
	private final Planet[] planets;
	private int left;

	public PlanetCard(Planet[] planets, int id) { // costruttore
		super(id, 0);
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
		return new PlanetCardResponse(this.planets[response.getId()]);
	}

}