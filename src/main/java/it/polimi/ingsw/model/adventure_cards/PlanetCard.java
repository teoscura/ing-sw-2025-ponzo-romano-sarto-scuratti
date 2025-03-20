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
	public Event setup()

	@Override
	public int apply(iSpaceShip state, iPlayerResponse response){
		//TODO
		return -days;
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