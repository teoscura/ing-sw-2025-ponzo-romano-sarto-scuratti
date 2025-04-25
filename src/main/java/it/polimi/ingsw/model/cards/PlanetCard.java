//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.model.cards.exceptions.AlreadyVisitedException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.PlanetAnnounceState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.Planet;

public class PlanetCard extends Card {
		
	private final ArrayList<Planet> planets;
	private int left;

	public PlanetCard(int id, int days, Planet[] planets) { // costruttore
		super(id, days);
		this.planets = new ArrayList<>(Arrays.asList(planets));
		this.left = planets.length;
	}

	@Override
	public CardState getState(VoyageState state) {
		return new PlanetAnnounceState(state, this, new ArrayList<>(state.getOrder(CardOrder.NORMAL)));
	}

	public List<Boolean> getVisited(){
		return this.planets.stream().map(p -> p.getVisited()).toList();
	}

	public Planet getPlanet(int id){
		return this.planets.get(id);
	}

	public int getSize(){
		return planets.size();
	}

	public void apply(Player p, int id) {
		if(p==null) throw new NullPointerException();
		if(id>=this.planets.size()) throw new ArgumentTooBigException( "Sent a planet id larger than the list.");
		if(id==-1){
			return;
		}
		if(this.planets.get(id).getVisited()) throw new AlreadyVisitedException();
		System.out.println(this.left);
		this.left--;
		System.out.println(this.left);
		this.planets.get(id).visit();
		if(left==0) this.exhaust();
	}

}