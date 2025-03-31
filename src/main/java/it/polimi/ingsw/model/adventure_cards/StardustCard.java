//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.MoveOnBoardMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class StardustCard extends Card {

    public StardustCard(int id){
        super(id, 0);
    }

    @Override
    public ClientMessage getRequest() {
        return null;
    }

    @Override
	public CardResponseType getResponse() {
		return CardResponseType.NONE;
	}

	@Override
	public CardResponseType getAfterResponse() {
		return CardResponseType.NONE;
	}

	@Override
	public CardOrder getOrder() {
		return CardOrder.INVERSE;
	}

	@Override
	public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) {
        if(model==null||ship==null) throw new NullPointerException();
		int lost_days = ship.countExposedConnectors();
        if(lost_days==0) return null;
		return new MoveOnBoardMessage(-ship.getEnginePower());
	}

}