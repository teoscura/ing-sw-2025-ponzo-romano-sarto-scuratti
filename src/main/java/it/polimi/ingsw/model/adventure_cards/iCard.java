//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.ModelInstance;

public interface iCard {
    public int getId();

    public ClientMessage getRequest();
    public CardResponseType getResponse();
    public CardResponseType getAfterResponse();
    public CardOrder getOrder();
    public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) throws PlayerNotFoundException;
    public boolean getExhausted();
}