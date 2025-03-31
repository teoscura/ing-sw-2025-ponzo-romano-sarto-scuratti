package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.AskRemoveCrewMessage;
import it.polimi.ingsw.message.client.AskTurnOnMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.FightRewardMessage;
import it.polimi.ingsw.message.client.GameLostMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SlaversCard extends Card{

    private final int min_power;
    private final int crew_penalty;
    private final int credits;

    public SlaversCard(int id, int days, int min_power, int crew_penalty, int credits){
        super(id, days);
        if(credits<=0||crew_penalty<=0) throw new NegativeArgumentException();
        this.min_power = min_power;
        this.crew_penalty = crew_penalty;
        this.credits = credits;
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
		return this.after_response;
	}

	@Override
	public CardOrder getOrder() {
		return CardOrder.NORMAL;
	}

	@Override
	public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) throws PlayerNotFoundException {
        if(model==null||ship==null) throw new NullPointerException();
		this.after_response = CardResponseType.NONE;
        if(ship.getCannonPower()>this.min_power){
            this.exhaust();
            this.after_response = CardResponseType.FIGHT_REWARD_CREDITS;
            return new FightRewardMessage(credits, days);
        }
        else if(ship.getCannonPower()==this.min_power){
            return null;
        }
        if(ship.getTotalCrew()<=this.crew_penalty){
            model.loseGame(ship.getColor());
            return new GameLostMessage();
        }
        return new AskRemoveCrewMessage(this.crew_penalty);
	}

}

