//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.message.client.AskTurnOnMessage;
import it.polimi.ingsw.message.client.BrokenCabinMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.FightRewardMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.iSpaceShip;


public class PiratesCard extends Card{

    private final ProjectileArray shots;
    private final int credits;
    private final int min_power;

    public PiratesCard(int id, int days, ProjectileArray shots, int min_power, int credits){
        super(id, days);
        if(shots == null) throw new NullPointerException();
        if(min_power<=0 || credits <=0) throw new NegativeArgumentException("Pirate power/rewards cannot be less than one.");
        this.credits = credits;
        this.shots = shots;
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
        if(model==null||ship==null) throw new NullPointerException();
        this.after_response = CardResponseType.NONE;
        if(ship.getCannonPower()>this.min_power){
            this.exhaust();
            this.after_response = CardResponseType.FIGHT_REWARD_CREDITS;
            return new FightRewardMessage(this.credits, this.days);
        }
        else if(ship.getCannonPower()==this.min_power){
            return null;
        }
        boolean broken_center_cabin = false;
        for(Projectile p : this.shots.getProjectiles()){
            broken_center_cabin = ship.handleShot(p);
        }
        if(broken_center_cabin) return new BrokenCabinMessage();
        return null;
    }
    
}
