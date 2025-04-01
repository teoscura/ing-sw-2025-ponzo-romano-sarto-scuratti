//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.SmugglersAnnounceState;
import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.player.Player;

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
    public CardState getState(ModelInstance model) {
        return new SmugglersAnnounceState(model, this);
    }

    public Planet getReward(){
        return this.reward;
    }

    public int getCargoPenalty(){
        return this.cargo_taken;
    }

	public boolean apply(ModelInstance model, Player p) {
		if(p==null) throw new NullPointerException();
        if(p.getSpaceShip().getCannonPower()>this.min_power){
            this.exhaust();
            return true;
        }
        else if(p.getSpaceShip().getCannonPower()==this.min_power){
            return true;
        }
        return false;
	}

}
