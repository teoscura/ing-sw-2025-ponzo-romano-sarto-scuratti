//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.PlanetAnnounceState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;

public class PlanetCard extends Card {
		
	private final Planet[] planets;
	private int left;

	public PlanetCard(int id, int days, Planet[] planets) { // costruttore
		super(id, days);
		this.planets = planets;
		this.left = planets.length;
	}

	@Override
	public CardState getState(ModelInstance model) {
		return new PlanetAnnounceState(ASAS);
	}

	public Planet getPlanet(int id){
		if(id<0||id>this.planets.length) throw new OutOfBoundsException("Id is not valid");
		return this.planets[id];
	}

	public Planet apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) {
		if(ship==null||response==null) throw new NullPointerException();
		if(response.getId()>=this.planets.length) throw new ArgumentTooBigException( "Sent a planet id larger than the list.");
		if(response.getId()==-1 || this.planets[response.getId()].getVisited()) return null;
		this.left--;
		this.planets[response.getId()].visit();
		if(left==0) this.exhaust();
		return this.planets[response.getId()];
	}

}