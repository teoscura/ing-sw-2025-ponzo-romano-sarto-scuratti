package it.polimi.ingsw.model.adventure_cards;

import java.util.ArrayList;

import it.polimi.ingsw.model.player.*;

public class PlanetCard extends Card {
	public PlanetCard(int id) { // costruttore

	}

	ArrayList<Planet> planets = new ArrayList<Planet>();
	int days_spent;

	void visit(PlayerColor current_player/* planche.getFirstPlayer() */) {
		/*
		 * chiedi di atterrare
		 * if yes
		 * load resources // load goods on ship
		 * player.position -= days_spent
		 */
		visit(Planche.getNextPlayer(current_player));
	}
}