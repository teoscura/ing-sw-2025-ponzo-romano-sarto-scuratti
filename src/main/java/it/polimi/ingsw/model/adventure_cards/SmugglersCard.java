//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.message.client.AskRemoveMerchMessage;
import it.polimi.ingsw.message.client.AskTurnOnMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.SmugglerRewardMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SmugglersCard extends Card{
    
    private final Planet reward;
    private final int cargo_taken;
    private final int min_power;
    

    public SmugglersCard(int id, int days, Planet reward, int cargo_taken, int min_power){
        super(id, days);
        if(reward == null) throw new NullPointerException();
        if(min_power<=0 || cargo_taken<=0) throw new NegativeArgumentException();
        this.reward = reward;
        this.cargo_taken = cargo_taken;
        this.min_power = min_power;
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
	public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) {
		if(ship==null) throw new NullPointerException();
        this.after_response = CardResponseType.NONE;
        if(ship.getCannonPower()>this.min_power){
            this.after_response = CardResponseType.FIGHT_REWARD_CARGO;
            this.exhaust();
            return new SmugglerRewardMessage(this.reward.getContains(), this.days);
        }
        else if(ship.getCannonPower()==this.min_power){
            return null;
        }
        this.after_response = CardResponseType.REMOVE_CREW;
        return new AskRemoveMerchMessage(this.cargo_taken);
	}

}
