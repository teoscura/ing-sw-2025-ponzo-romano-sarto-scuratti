package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.AskTurnOnMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.GameLostMessage;
import it.polimi.ingsw.message.client.MoveOnBoardMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class OpenSpaceCard extends Card {
    
    protected OpenSpaceCard(int id) {
        super(id, 0);
    }

    @Override
    public ClientMessage getRequest() {
        return new AskTurnOnMessage();
    }

    @Override
    public CardResponseType getResponse() {
        return CardResponseType.TURNON_ACCEPT;
    }

    @Override
    public CardResponseType getAfterResponse() {
        return CardResponseType.NONE;
    }

    @Override
    public CardOrder getOrder() {
        return CardOrder.NORMAL;
    }

    @Override
    public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) throws PlayerNotFoundException {
        if(ship==null) throw new NullPointerException();
        if(ship.getEnginePower()>0){
            model.getPlanche().movePlayer(ship, ship.getEnginePower());
            return new MoveOnBoardMessage(ship.getEnginePower());
        }
        model.loseGame(ship.getColor());
        return new GameLostMessage();
    }

}
