//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.PiratesAnnounceState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;


public class PiratesCard extends Card {

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
    public CardState getState(VoyageState state) {
        return new PiratesAnnounceState(state, this, state.getOrder(CardOrder.NORMAL));
    }

    public int getCredits(){
        return this.credits;
    }

    public ProjectileArray getShots(){
        return this.shots;
    }

    public boolean apply(VoyageState state, Player p) {
        if(state==null||p==null) throw new NullPointerException();
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
