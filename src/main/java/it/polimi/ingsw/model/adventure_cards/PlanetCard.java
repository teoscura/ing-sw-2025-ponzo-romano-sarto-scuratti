//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.PlanetAnnounceState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
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
	public CardState getState(VoyageState state) {
		return new PlanetAnnounceState(state, this, state.getOrder(CardOrder.NORMAL));
	}

	public Planet getPlanet(int id){
		if(id<0||id>this.planets.length) throw new OutOfBoundsException("Id is not valid");
		return this.planets[id];
	}

	public void apply(Player p, int id) {
		if(p==null) throw new NullPointerException();
		if(id>=this.planets.length) throw new ArgumentTooBigException( "Sent a planet id larger than the list.");
		if(id==-1 || this.planets[id].getVisited());
		this.left--;
		this.planets[id].visit();
		if(left==0) this.exhaust();
	}

}