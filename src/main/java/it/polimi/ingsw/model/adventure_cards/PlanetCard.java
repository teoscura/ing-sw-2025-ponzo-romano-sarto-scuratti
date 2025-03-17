package it.polimi.ingsw.model.adventure_cards;

import java.util.ArrayList;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.board.*;
public class PlanetCard extends Card {
	public PlanetCard(int id) { // costruttore

	}

	ArrayList<Planet> planets = new ArrayList<Planet>();
	int days_spent;

	public void visitPlanet(PlayerColor current_player/* planche.getFirstPlayer() */) {
		/*
		 * chiedi di atterrare
		 * if yes
		 * load resources // load goods on ship
		 * player.position -= days_spent
		 */
		visitPlanet(Planche.getNextPlayer(current_player));
	}
}