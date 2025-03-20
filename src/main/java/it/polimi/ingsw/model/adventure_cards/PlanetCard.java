package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.adventure_cards.events.vPlanetInfoEvent;
import it.polimi.ingsw.model.adventure_cards.exceptions.CoordsIndexLenghtMismatchException;

public class PlanetCard extends Card {
	
	Planet[] planets;
	
	public PlanetCard(Planet[] planets, int id) { // costruttore
		super(id, 0);
		this.planets = planets;
	}

	@Override
	public iCEvent setup(iSpaceShip ship){
		return new vPlanetInfoEvent(this.planets);
	}

	@Override
	public int apply(iSpaceShip state, iPlayerResponse response){
		if(response.getId()>=this.planets.length) throw new ArgumentTooBigException( "Sent a planet id larger than the list.");
		if(response.getId()==-1) return 0;
		validateCargoChoices(response.getCoordArray(), response.getMerchChoices(), response.getId());
		//TODO loading.

		
		this.planets[response.getId()].visit();
		return -this.planets[response.getId()].getDays();
	}

	private void validateCargoChoices(ShipCoords[] coords, int[] cargo_indexes, int id){
        //TODO. throw exceptions where needed.
        if(coords.length!=cargo_indexes.length) throw new CoordsIndexLenghtMismatchException("Storage coords and cargo locations aren't the same lenght.");
		if(coords.length>this.planets[id].getTotalQuantity()) throw new ArgumentTooBigException("Sent more shipment destinations than the amount present on the planet.");
		
	}

	// public void visitPlanet(PlayerColor current_player/* planche.getFirstPlayer() */) {
	// 	/*
	// 	 * chiedi di atterrare
	// 	 * if yes
	// 	 * load resources // load goods on ship
	// 	 * player.position -= days_spent
	// 	 */
	// 	visitPlanet(Planche.getNextPlayer(current_player));
	// }
}