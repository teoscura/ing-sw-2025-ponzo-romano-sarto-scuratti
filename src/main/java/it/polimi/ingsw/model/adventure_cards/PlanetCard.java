package it.polimi.ingsw.model.adventure_cards;

import java.util.ArrayList;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.board.*;
public class PlanetCard extends Card {
	
	private int days;
	Planet[] planets;
	
	public PlanetCard(Planet[] planets, int id) { // costruttore
		super(id);
		this.planets = planets;
	}

	@Override
	public void apply(iSpaceShip state, iPlayerResponse response){

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