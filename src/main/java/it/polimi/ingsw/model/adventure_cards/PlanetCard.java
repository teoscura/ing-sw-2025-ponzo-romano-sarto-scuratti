//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.message.client.AskLandingMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.PlanetMessage;
import it.polimi.ingsw.model.ModelInstance;
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
    public ClientMessage getRequest() {
        return new AskLandingMessage(planets, this.days);
    }

	@Override
	public CardResponseType getResponse() {
		return CardResponseType.LAND_CHOICE;
	}

	@Override
	public CardResponseType getAfterResponse() {
		return this.after_response;
	}

	@Override
	public CardOrder getOrder() {
		return CardOrder.NORMAL;
	}

	@Override
	public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) {
		if(ship==null||response==null) throw new NullPointerException();
		if(response.getId()>=this.planets.length) throw new ArgumentTooBigException( "Sent a planet id larger than the list.");
		this.after_response=CardResponseType.NONE;
		if(response.getId()==-1 || this.planets[response.getId()].getVisited()) return null;
		this.after_response = CardResponseType.TAKE_CARGO;
		this.left--;
		if(left==0) this.exhaust();
		return new PlanetMessage(this.planets[response.getId()].getContains(), this.days);
	}

}